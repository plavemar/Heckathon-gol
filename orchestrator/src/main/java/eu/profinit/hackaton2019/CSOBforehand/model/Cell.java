package eu.profinit.hackaton2019.CSOBforehand.model;

import java.util.List;

public class Cell {

    private int state;
    private int generation;
    private Position position;
    private List<Integer> neighbors;

    public Cell(int state, int generation, Position position) {
        this.state = state;
        this.generation = generation;
        this.position = position;
    }

    public List<Integer> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public int getState() {
        return state;
    }

    public int getGeneration() {
        return generation;
    }

    public Position getPosition() {
        return position;
    }
}
