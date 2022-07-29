package ua.net.maxx.mqtt2modbus.config;

import java.util.List;
import ua.net.maxx.mqtt2modbus.modbus.ModbusRegisterDescription;

public class Device {
    private int deviceId;
    private String topic;
    private List<ModbusRegisterDescription> data;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<ModbusRegisterDescription> getData() {
        return data;
    }

    public void setData(List<ModbusRegisterDescription> data) {
        this.data = data;
    }
}
