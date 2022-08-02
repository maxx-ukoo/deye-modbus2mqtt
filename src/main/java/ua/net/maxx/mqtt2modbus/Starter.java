package ua.net.maxx.mqtt2modbus;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import org.yaml.snakeyaml.Yaml;
import ua.net.maxx.mqtt2modbus.influx.InfluxDBStorageService;
import ua.net.maxx.mqtt2modbus.modbus.ModbusService;
import ua.net.maxx.mqtt2modbus.config.Config;
import ua.net.maxx.mqtt2modbus.modbus.impl.ModbusServiceImpl;
import ua.net.maxx.mqtt2modbus.mqtt.MqttSender;
import ua.net.maxx.mqtt2modbus.mqtt.impl.MqttSenderImpl;
import ua.net.maxx.mqtt2modbus.timer.BridgeTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Starter {

    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new BufferedInputStream(new FileInputStream("config.yaml"));
            Config config = yaml.loadAs(inputStream, Config.class);

            SerialParameters portParams = new SerialParameters();
            portParams.setPortName(config.getModbus().getPort());
            portParams.setBaudRate(config.getModbus().getRate());
            portParams.setStopbits(1);
            portParams.setParity(0);
            portParams.setEncoding(Modbus.SERIAL_ENCODING_RTU);
            MqttSender mqttSender = new MqttSenderImpl("tcp://mqtt.sng.maxx");
            ModbusService modbusService = new ModbusServiceImpl(portParams);

            String token = "EBaj2gOeS6xuH4bba9L0R5H3qt2IXFPAfDd2CvyZJJM5Uyjoq96OE8I1c-UvdiZnT1mutcP33C8I0wbHqjHlvw==";
            String url = "http://influx-db.sng.maxx:8086/";
            String org = "sng-home";
            String bucket = "energy";

            InfluxDBStorageService influxDb = new InfluxDBStorageService(config.getInflux());
            BridgeTask bridgeTask = new BridgeTask(mqttSender, modbusService, config);
            bridgeTask.addListener(influxDb);
            Timer timer1 = new Timer();
            timer1.schedule(bridgeTask, Starter.getNextStartDate(), 10000);
            logger.info("Bridge Timer scheduled");
        } catch (Exception e) {
            logger.error("Error starting app", e);
        }
    }

    private static Date getNextStartDate() {
        Date d = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        c.add(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 5);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();

    }
}
