package ua.net.maxx.mqtt2modbus.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import ua.net.maxx.mqtt2modbus.config.Device;
import ua.net.maxx.mqtt2modbus.modbus.ModbusService;
import ua.net.maxx.mqtt2modbus.config.Config;
import ua.net.maxx.mqtt2modbus.mqtt.MqttSender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BridgeTask extends TimerTask {

    private final List<DataListener> listeners = new ArrayList<>();

    private static Logger logger = LogManager.getLogger();
    private final MqttSender mqttSender;
    private final ModbusService modbusService;
    private final List<Device> devices;

    public BridgeTask(MqttSender mqttSender, ModbusService modbusService, Config config) {
        this.mqttSender = mqttSender;
        this.modbusService = modbusService;
        this.devices = config.getDevices();
    }

    @Override
    public void run() {
        logger.debug("Task started");
        devices.forEach(device -> {
            Map<String, String> data = modbusService.getData(device);
            data.entrySet().forEach(entry -> {
                listeners.forEach(listener -> listener.publish(entry.getKey(), entry.getValue()));
                mqttSender.send(entry.getKey(), entry.getValue());
                logger.trace("topic: {}, payload: {}", entry.getKey(), entry.getValue());
            });
        });
        logger.debug("Task finished");
    }

    public void addListener(DataListener listener) {
        this.listeners.add(listener);
    }
}
