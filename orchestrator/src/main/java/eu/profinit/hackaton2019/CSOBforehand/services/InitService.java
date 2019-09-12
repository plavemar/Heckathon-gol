package eu.profinit.hackaton2019.CSOBforehand.services;

import eu.profinit.hackaton2019.CSOBforehand.model.Cell;
import eu.profinit.hackaton2019.CSOBforehand.model.Position;
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

    public List<List<Cell>> generateFirstGen() {
        int generation = 0;
        Map<Integer, String> initMap = new HashMap<>();

        initMap.put(0, "0000000000");
        initMap.put(1, "0000000000");
        initMap.put(2, "0000000000");
        initMap.put(3, "0000000000");
        initMap.put(4, "0000000000");
        initMap.put(5, "0000000000");
        initMap.put(6, "0000000000");
        initMap.put(7, "0000000000");
        initMap.put(8, "0000000000");
        initMap.put(9, "0000000000");

        return initMap.entrySet().stream()
                      .map(entry -> toCellRow(entry.getValue(), entry.getKey(), generation))
                      .collect(Collectors.toList());
    }

    private List<Cell> toCellRow(String cellRowDefinition, Integer rowNumber, Integer generation) {
        List<Cell> result = new ArrayList<>();

        for (int i = 0; i < cellRowDefinition.length(); i++) {
            result.add(new Cell(cellRowDefinition.charAt(i), generation, new Position(i, rowNumber)));
        }

        return result;
    }

    @Override
    public void run(final String... args) {
        generateFirstGen();
    }
}
