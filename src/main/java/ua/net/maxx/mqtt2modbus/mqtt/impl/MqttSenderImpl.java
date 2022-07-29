package ua.net.maxx.mqtt2modbus.mqtt.impl;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import ua.net.maxx.mqtt2modbus.mqtt.MqttSender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MqttSenderImpl implements MqttSender {

    private static Logger logger = LogManager.getLogger();

    private final IMqttClient client;

    public MqttSenderImpl(String url) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        client = new MqttClient(url, publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
    }

    @Override
    public void send(String topic, String payload) {
        if (!client.isConnected()) {
            return;
        }
        MqttMessage msg = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
        msg.setQos(0);
        msg.setRetained(true);
        try {
            client.publish(topic, msg);
        } catch (MqttException e) {
            logger.error("Error publish MQTT data", e);
        }
    }
}
