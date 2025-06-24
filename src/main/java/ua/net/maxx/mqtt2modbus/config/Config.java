package ua.net.maxx.mqtt2modbus.config;

import java.util.List;

public class Config {
    private ModbusPort modbus;
    private InfluxDbConfig influx;

    private MqttConfig mqtt;

    private List<Device> devices;

    public ModbusPort getModbus() {
        return modbus;
    }

    public void setModbus(ModbusPort modbus) {
        this.modbus = modbus;
    }

    public MqttConfig getMqtt() {
        return mqtt;
    }

    public void setMqtt(MqttConfig mqtt) {
        this.mqtt = mqtt;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public InfluxDbConfig getInflux() {
        return influx;
    }

    public void setInflux(InfluxDbConfig influx) {
        this.influx = influx;
    }

    @Override
    public String toString() {
        return "Config{" +
                "modbus=" + modbus +
                ", influx=" + influx +
                ", mqtt=" + mqtt +
                ", devices=" + devices +
                '}';
    }
}