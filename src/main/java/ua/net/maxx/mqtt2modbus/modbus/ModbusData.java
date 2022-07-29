package ua.net.maxx.mqtt2modbus.modbus;

import java.math.BigDecimal;
import ua.net.maxx.mqtt2modbus.config.ValueType;

public class ModbusData {
    private int offset;
    private String name;
    private String value;
    private BigDecimal factor;
    private String topic;
    private ValueType type;

    public ModbusData() {

    }

    public ModbusData(int offset, String name, BigDecimal factor) {
        this.offset = offset;
        this.name = name;
        this.factor = factor;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOffset() {
        return offset;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }
}
