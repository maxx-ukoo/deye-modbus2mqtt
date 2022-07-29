package ua.net.maxx.mqtt2modbus.mqtt;

public interface MqttSender {
    void send(String topic, String value);
}
