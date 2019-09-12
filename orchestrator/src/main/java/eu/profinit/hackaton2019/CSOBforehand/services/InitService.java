package eu.profinit.hackaton2019.CSOBforehand.services;

import eu.profinit.hackaton2019.CSOBforehand.model.Cell;
import eu.profinit.hackaton2019.CSOBforehand.model.InitBoard;
import eu.profinit.hackaton2019.CSOBforehand.model.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitService implements CommandLineRunner {
    public static Integer BOARD_SIZE = 10;

    private final MessagingService messagingService;
    private final CollectService collectService;

    static {
        Map<Integer, String> stableMap = new HashMap<>();
        stableMap.put(0, "110011000");
        stableMap.put(1, "110100100");
        stableMap.put(2, "000011000");
        stableMap.put(3, "011000000");
        stableMap.put(4, "100101100");
        stableMap.put(5, "010101010");
        stableMap.put(6, "001010100");
        stableMap.put(7, "000101000");
        stableMap.put(8, "000010000");

        Map<Integer, String> oscilators1 = new HashMap<>();
        stableMap.put(0, "0000000000000000000000");
        stableMap.put(1, "0000001110000000000000");
        stableMap.put(2, "0111011100000000000000");
        stableMap.put(3, "0000000000000000000000");
        stableMap.put(4, "1100000000000000111000");
        stableMap.put(5, "1100000000000000101000");
        stableMap.put(6, "0011000000000000111000");
        stableMap.put(7, "0011000000000000111000");
        stableMap.put(8, "0000000000000000111000");
        stableMap.put(9, "0000000000000000111000");
        stableMap.put(10, "0000000000000000101000");
        stableMap.put(11, "0000000000000000111000");
        stableMap.put(12, "0000000000000000000000");
        stableMap.put(13, "0000000000000000000000");
        stableMap.put(14, "0000000000000000000000");
        stableMap.put(15, "0000000000000000000000");

        Map<Integer, String> oscilators2 = new HashMap<>();
        stableMap.put(8, "000000000000000000000000000000");

        Map<Integer, String> spaceships1 = new HashMap<>();
        stableMap.put(8, "000000000000000000000000000000");

        Map<Integer, String> spaceships2 = new HashMap<>();
        stableMap.put(8, "000000000000000000000000000000");
    }

    public List<List<Cell>> generateFirstGen(InitBoard initBoard, Integer randomSize) {
        int generation = 0;
        Map<Integer, String> initMap = new HashMap<>();

        initMap.put(0, "110011000");
        initMap.put(0, "110100100");
        initMap.put(0, "000011000");
        initMap.put(0, "011000000");
        initMap.put(0, "100101100");
        initMap.put(0, "010101010");
        initMap.put(0, "001010100");
        initMap.put(0, "000101000");
        initMap.put(0, "000010000");

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
        messagingService.sendFirstGeneration(collectService, this);
        collectService.receiveFromCollect(messagingService);
    }
}
