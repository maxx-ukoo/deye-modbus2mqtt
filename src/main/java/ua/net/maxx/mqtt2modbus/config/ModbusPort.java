package ua.net.maxx.mqtt2modbus.config;

public class ModbusPort {
    private String port;
    private int rate;

    public String getPort() {
        return port;
    }

    public int getRate() {
        return rate;
    }

    public void setPort(String name) {
        this.port = name;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
