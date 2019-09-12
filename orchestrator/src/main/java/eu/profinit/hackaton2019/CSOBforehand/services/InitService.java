package eu.profinit.hackaton2019.CSOBforehand.services;

import eu.profinit.hackaton2019.CSOBforehand.model.Cell;
import eu.profinit.hackaton2019.CSOBforehand.model.InitBoard;
import eu.profinit.hackaton2019.CSOBforehand.model.Position;
import lombok.NonNull;
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
    public static Integer BOARD_SIZE;

    private final MessagingService messagingService;
    private final CollectService collectService;

    private static Map<Integer, String> stables = new HashMap<>();
    private static Map<Integer, String> oscilators = new HashMap<>();
    private static Map<Integer, String> spaceships = new HashMap<>();

    static {
        stables.put(0, "110011000");
        stables.put(1, "110100100");
        stables.put(2, "000011000");
        stables.put(3, "011000000");
        stables.put(4, "100101100");
        stables.put(5, "010101010");
        stables.put(6, "001010100");
        stables.put(7, "000101000");
        stables.put(8, "000010000");
        oscilators.put(0, "0000000000000000");
        oscilators.put(1, "0000001110000000");
        oscilators.put(2, "0111011100000000");
        oscilators.put(3, "0000000000000000");
        oscilators.put(4, "1100000000111000");
        oscilators.put(5, "1100000000101000");
        oscilators.put(6, "0011000000111000");
        oscilators.put(7, "0011000000111000");
        oscilators.put(8, "0000000000111000");
        oscilators.put(9, "0000000000111000");
        oscilators.put(10, "0000000000101000");
        oscilators.put(11, "0000000000111000");
        oscilators.put(12, "0000000000000000");
        oscilators.put(13, "0000000000000000");
        oscilators.put(14, "0000000000000000");
        oscilators.put(15, "0000000000000000");
        spaceships.put(0, "001000000000000");
        spaceships.put(1, "101000000000000");
        spaceships.put(2, "011000000000000");
        spaceships.put(3, "000000000000000");
        spaceships.put(4, "000000000000000");
        spaceships.put(5, "000000000000000");
        spaceships.put(6, "000000000000000");
        spaceships.put(7, "000000000000000");
        spaceships.put(8, "000000000000000");
        spaceships.put(9, "000000000000000");
        spaceships.put(10, "000000000000000");
        spaceships.put(11, "011110000000000");
        spaceships.put(12, "100010000000000");
        spaceships.put(13, "000010000000000");
        spaceships.put(14, "100100000000000");
    }

    public List<List<Cell>> generateFirstGen(@NonNull InitBoard initBoard, Integer randomSize) {
        int generation = 0;

        Map<Integer, String> initMap;

        switch (initBoard) {
            case STABLE:
                BOARD_SIZE = stables.size();
                initMap = stables;
                break;
            case OSCILATORS:
                BOARD_SIZE = oscilators.size();
                initMap = oscilators;
                break;
            case SPACESHIPS:
                BOARD_SIZE = spaceships.size();
                initMap = spaceships;
                break;
            case RANDOM:
                BOARD_SIZE = randomSize;
                initMap = new HashMap<>();
                for (int i = 0; i < randomSize; i++) {
                    String row = "";
                    for (int j = 0; j < randomSize; j++) {
                        row = row.concat((String.valueOf(Math.round(Math.random()))));
                    }
                    initMap.put(i, row);
                }
                break;
            default:
                return null;
        }

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
