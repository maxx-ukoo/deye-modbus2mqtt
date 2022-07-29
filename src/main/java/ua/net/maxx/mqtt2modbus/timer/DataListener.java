package ua.net.maxx.mqtt2modbus.timer;

public interface DataListener {
    void publish(String topic, String value);
}
