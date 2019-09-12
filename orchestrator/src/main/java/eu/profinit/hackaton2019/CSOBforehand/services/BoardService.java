package eu.profinit.hackaton2019.CSOBforehand.services;

import eu.profinit.hackaton2019.CSOBforehand.model.Cell;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    public List<List<Cell>> calculateNeighbours(List<List<Cell>> input) {
        for (int i = 0; i < InitService.BOARD_SIZE; i++) {
            for (int j = 0; j < InitService.BOARD_SIZE; j++) {
                List<Integer> neighbours = new ArrayList<>();
                Cell currentCell = input.get(i).get(j);

                neighbours.add(getCellState(i - 1, j - 1, currentCell));
                neighbours.add(getCellState(i, j - 1, currentCell));
                neighbours.add(getCellState(i + 1, j - 1, currentCell));

                neighbours.add(getCellState(i - 1, j, currentCell));
                neighbours.add(getCellState(i + 1, j, currentCell));

                neighbours.add(getCellState(i - 1, j + 1, currentCell));
                neighbours.add(getCellState(i, j + 1, currentCell));
                neighbours.add(getCellState(i + 1, j + 1, currentCell));

                input.get(i).get(j).setNeighbors(neighbours);
            }
        }

        return input;
    }

    private Integer getCellState(int x, int y, Cell cell) {
        if (x < 0 || y < 0 || x >= InitService.BOARD_SIZE || y >= InitService.BOARD_SIZE) {
            return 0;
        }

        return cell.getState();
    }
}
