package ua.net.maxx.mqtt2modbus.modbus;

import java.util.List;

public class ModbusRegisterDescription {
    private String topic;
    private int startRegister;
    private int numberRegisters;
    private int type;
    private List<ModbusData> registers;

    /*public ModbusRegisterDescription(int deviceId, int type, int startRegister, int numberRegisters, List<ModbusData> data) {
        this.deviceId = deviceId;
        this.startRegister = startRegister;
        this.numberRegisters = numberRegisters;
        this.type = type;
        this.data = data;
    }*/

    public int getStartRegister() {
        return startRegister;
    }

    public int getType() {
        return type;
    }

    public int getNumberRegisters() {
        return numberRegisters;
    }

    public void setStartRegister(int startRegister) {
        this.startRegister = startRegister;
    }

    public void setNumberRegisters(int numberRegisters) {
        this.numberRegisters = numberRegisters;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<ModbusData> getRegisters() {
        return registers;
    }

    public void setRegisters(List<ModbusData> registers) {
        this.registers = registers;
    }
}
