package eu.profinit.hackaton2019.CSOBforehand.services;

import eu.profinit.hackaton2019.CSOBforehand.model.Cell;
import eu.profinit.hackaton2019.CSOBforehand.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InitService implements CommandLineRunner {
    public static final Integer BOARD_SIZE = 10;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private CollectService collectService;

    public List<List<Cell>> generateFirstGen() {
        int generation = 0;
        Map<Integer, String> initMap = new HashMap<>();

        initMap.put(0, "1000000000");
        initMap.put(1, "0100000000");
        initMap.put(2, "0010000000");
        initMap.put(3, "0001000100");
        initMap.put(4, "0000101000");
        initMap.put(5, "0000010000");
        initMap.put(6, "0000000100");
        initMap.put(7, "1110000000");
        initMap.put(8, "0000110000");
        initMap.put(9, "0001110000");

        return initMap.entrySet().stream()
                      .map(entry -> toCellRow(entry.getValue(), entry.getKey(), generation))
                      .collect(Collectors.toList());
    }

    private List<Cell> toCellRow(String cellRowDefinition, Integer rowNumber, Integer generation) {
        List<Cell> result = new ArrayList<>();

        for (int i = 0; i < cellRowDefinition.length(); i++) {
            result.add(new Cell(Integer.parseInt(Character.toString(cellRowDefinition.charAt(i))), generation, new Position(i, rowNumber)));
        }

        return result;
    }

    @Override
    public void run(String... args) throws Exception {
        messagingService.sendFirstGeneration();
        collectService.receiveFromCollect(messagingService);
    }
}
