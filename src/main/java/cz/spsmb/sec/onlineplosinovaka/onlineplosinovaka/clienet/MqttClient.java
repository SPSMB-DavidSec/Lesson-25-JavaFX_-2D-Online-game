package cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.clienet;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MqttClient {
    Mqtt5BlockingClient client;
    public static final String TOPIC = "test/SPSMB/PRO/2D_GRAPHICS";
    public static final String CLIENT_ID = "David Šec";

    public void connect() {
        client = Mqtt5Client.builder()
                .identifier(CLIENT_ID)
                .serverHost("broker.hivemq.com")
                .buildBlocking();

        client.connect();

    }

    public void subscribe(ClientEventHandler clientEventHandler) {
        client.toAsync().subscribeWith()
                .topicFilter(TOPIC)
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback((data) -> clientEventHandler.onMessageReceived(new Message(data.getPayloadAsBytes())))
                .send();
        System.out.println("subscribed");
    }

    public void sendMessage(Message message) {
        client.publishWith()
                .topic(TOPIC)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(message.getContent())
                .send();

        System.out.println("message send!");
    }
    public void sendMessage(String message) {
        client.publishWith()
                .topic(TOPIC)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(message.getBytes(StandardCharsets.UTF_8))
                .send();

        System.out.println("---> " + message);
    }

}