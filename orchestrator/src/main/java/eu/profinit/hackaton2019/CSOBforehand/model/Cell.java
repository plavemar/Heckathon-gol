package eu.profinit.hackaton2019.CSOBforehand.model;

public class Cell {

    private int state;
    private int generation;
    private Position position;
    private int[] neighbors;

    public Cell(int state, int generation, Position position) {
        this.state = state;
        this.generation = generation;
        this.position = position;
    }

    public int[] getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(int[] neighbors) {
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
