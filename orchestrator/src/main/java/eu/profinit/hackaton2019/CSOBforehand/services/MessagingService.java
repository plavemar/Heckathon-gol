package eu.profinit.hackaton2019.CSOBforehand.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MessagingService implements CommandLineRunner {
    @Autowired
    private InitService initService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendFirstGeneration() throws IOException, ExecutionException, InterruptedException {
        List<String> firstGen = boardService.calculateNeighbours(initService.generateFirstGen()).stream()
                                            .flatMap(List::stream)
                                            .map(cell -> {
                                                try {
                                                    return objectMapper.writeValueAsString(cell);
                                                } catch (JsonProcessingException e) {
                                                    e.printStackTrace();
                                                    return null;
                                                }
                                            })
                                            .collect(Collectors.toList());

        ProjectTopicName topicName = ProjectTopicName.of("hackaton2019-forehand", "CREATE");
        Publisher publisher = null;
        List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

        try {
            publisher = Publisher.newBuilder(topicName).build();

            for (String jsonedCell : firstGen) {
                ByteString data = ByteString.copyFromUtf8(jsonedCell);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
                messageIdFutures.add(messageIdFuture);
            }
        } finally {
            List<String> messageIds = ApiFutures.allAsList(messageIdFutures).get();

            for (String messageId : messageIds) {
                System.out.println("published with message ID: " + messageId);
            }

            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

    @Override
    public void run(final String... args) throws Exception {
        sendFirstGeneration();
    }
}
