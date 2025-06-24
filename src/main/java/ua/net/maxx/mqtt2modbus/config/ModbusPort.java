package ua.net.maxx.mqtt2modbus.config;

public class ModbusPort {
    private String port;
    private int rate;

    private int poolTime;

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

    public int getPoolTime() {
        return poolTime;
    }

    public void setPoolTime(int poolTime) {
        this.poolTime = poolTime;
    }

    @Override
    public String toString() {
        return "ModbusPort{" +
                "port='" + port + '\'' +
                ", rate=" + rate +
                ", poolTime=" + poolTime +
                '}';
    }
}
