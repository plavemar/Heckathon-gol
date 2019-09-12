package eu.profinit.hackaton2019.CSOBforehand.services;

import com.google.api.client.util.Lists;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CollectService implements CommandLineRunner {

    @Autowired
    private MessagingService service;

    private List<String> data = Lists.newArrayList();

    private void receiveFromCollect() {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of("hackaton2019-forehand", "CREATE_SUB");

        MessageReceiver receiver =
                new MessageReceiver() {
                    @Override
                    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
                        // handle incoming message, then ack/nack the received message
                        System.out.println("Data received: " + message.getData().toStringUtf8());
                        data.add(message.getData().toStringUtf8());
                        consumer.ack();

                        if (data.size() == 100) {
                            System.out.println("Received 100th message");
                            try {
                                service.sendNextGeneration(data);
                            } catch (IOException | ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            subscriber.startAsync().awaitRunning();
            // Allow the subscriber to run indefinitely unless an unrecoverable error occurs
            subscriber.awaitTerminated();
        } finally {
            if (subscriber != null) {
                subscriber.stopAsync();
            }
        }
    }

    @Override
    public void run(final String... args) throws Exception {
        receiveFromCollect();
    }
}
