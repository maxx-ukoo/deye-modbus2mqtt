package ua.net.maxx.mqtt2modbus;

import com.fazecast.jSerialComm.SerialPort;
import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Ignore;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import ua.net.maxx.mqtt2modbus.modbus.ModbusService;
import ua.net.maxx.mqtt2modbus.config.Config;
import ua.net.maxx.mqtt2modbus.modbus.impl.ModbusServiceImpl;
import ua.net.maxx.mqtt2modbus.mqtt.MqttSender;
import ua.net.maxx.mqtt2modbus.mqtt.impl.MqttSenderImpl;


@Ignore
public class AppTest {

    @Test
    @Ignore
    public void fullTest() throws Exception {
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
        config.getDevices().forEach(device -> {
            Map<String, String> data = new TreeMap<>(modbusService.getData(device));
            data.entrySet().forEach(entry -> {
                System.out.println(String.format("%s => %s", entry.getKey(), entry.getValue()));
                mqttSender.send(entry.getKey(), entry.getValue());
            });

            data.entrySet().stream()
                    .filter(entry -> entry.getKey().contains("total"))
                    .filter(entry -> entry.getKey().contains("power"))
                    .forEach(entry -> {
                        System.out.println(String.format("%s => %s", entry.getKey(), entry.getValue()));
                        /*String topic = entry.getKey();
                        String id = "SNG_" + topic.replace("/", "_");
                        String item = "  - id: " + id + "\n" +
                        "    channelTypeUID: mqtt:number\n" +
                        "    label: " + topic.replace("/", "_") + "\n" +
                        "    description: \"\"\n" +
                        "    configuration:\n" +
                        "      stateTopic: " + topic + "\n";
                        System.out.println(item);*/
                    });
        });


    }

    @Test
    public void sendDataTest() {

        SerialParameters portParams = new SerialParameters();
        portParams.setPortName("COM3");
        portParams.setBaudRate(115200);
        portParams.setStopbits(1);
        portParams.setParity(0);
        portParams.setEncoding(Modbus.SERIAL_ENCODING_RTU);

        byte[] requestArray_v0 = {0x4E, 0x57, 0x00, 0x13, 0x00, 0x00, 0x00, 0x00, 0x06, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x68, 0x00, 0x00, 0x01, 0x29};
        SerialConnection jSerialCommPort = new SerialConnection(portParams);
        try {
            jSerialCommPort.open();
            for (int i=0; i<100000; i++) {
                jSerialCommPort.writeBytes(requestArray_v0, 1);
                Thread.sleep(20);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
