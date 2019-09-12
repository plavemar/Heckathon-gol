package eu.profinit.hackaton2019.CSOBforehand.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import eu.profinit.hackaton2019.CSOBforehand.gol.GolService;
import eu.profinit.hackaton2019.CSOBforehand.gol.ReqState;
import eu.profinit.hackaton2019.CSOBforehand.gol.Request;
import eu.profinit.hackaton2019.CSOBforehand.model.Cell;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static eu.profinit.hackaton2019.CSOBforehand.services.InitService.BOARD_SIZE;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService  {

    private final InitService initService;
    private final BoardService boardService;
    private final ObjectMapper objectMapper;
    private final GolService golService;

    public void sendFirstGeneration() throws IOException, ExecutionException, InterruptedException {
        System.out.println("FIRST");
        golService.clearHistory();
        List<List<Cell>> nextGeneration = boardService.calculateNeighbours(initService.generateFirstGen());
        visualize(nextGeneration);
        publishCreate(nextGeneration);
    }

    public void sendNextGeneration(List<String> jsonGeneration) throws IOException, ExecutionException, InterruptedException {
        System.out.println("NEXT");
        List<List<Cell>> nextGeneration = boardService.calculateNeighbours(toObjectGeneration(jsonGeneration));
        visualize(nextGeneration);
        publishCreate(nextGeneration);
        System.out.println("MOVE");
    }

    private void publishCreate(List<List<Cell>> generation) throws IOException, ExecutionException, InterruptedException {
        TimeUnit.SECONDS.sleep(1);

        ProjectTopicName topicName = ProjectTopicName.of("hackaton2019-forehand", "CREATE");
        Publisher publisher = null;
        List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

        try {
            publisher = Publisher.newBuilder(topicName).build();

            for (String jsonedCell : toJsonGeneration(generation)) {
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

    private List<String> toJsonGeneration(List<List<Cell>> generation) {
        return generation.stream()
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
    }

    private List<List<Cell>> toObjectGeneration(List<String> generation) {
        List<List<Cell>> result = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            result.add(new ArrayList<>(BOARD_SIZE));
        }

        List<Cell> cells = generation.stream().map(cell -> {
            try {
                return objectMapper.readValue(cell, Cell.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        for (Cell cell : cells) {
            result.get(cell.getPosition().getY())
                  .add(cell.getPosition().getX(), cell);
        }

        return result;
    }

    private void visualize(List<List<Cell>> cells) {
        Request request = new Request();
        ReqState reqState = new ReqState();
        request.setState(reqState);
        reqState.setItemsCount(BOARD_SIZE);
        List<List<Boolean>> states = cells
                .stream()
                .map(l -> l.stream()
                           .map(c -> c.getState() == 1 ? true : false)
                           .collect(Collectors.toList()))
                .collect(Collectors.toList());
        reqState.setValues(states);
        golService.send(request);
    }
}
