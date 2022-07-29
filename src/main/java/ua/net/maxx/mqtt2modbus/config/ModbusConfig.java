package ua.net.maxx.mqtt2modbus.config;

import java.util.List;
import ua.net.maxx.mqtt2modbus.modbus.ModbusRegisterDescription;

public class ModbusConfig {
    private ModbusPort port;
    private List<ModbusRegisterDescription> registers;

    public ModbusPort getPort() {
        return port;
    }

    public void setPort(ModbusPort port) {
        this.port = port;
    }

    public List<ModbusRegisterDescription> getRegisters() {
        return registers;
    }

    public void setRegisters(List<ModbusRegisterDescription> registers) {
        this.registers = registers;
    }
}
