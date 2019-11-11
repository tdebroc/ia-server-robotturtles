package com.grooptown.snorkunking.service.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibautdebroca on 11/11/2017.
 */
public class Level {

    private List<Cell> cells;

    public Level() {

    }
    public Level(int minTreasureCount, int maxTreasureCount, int numberOfCells) {
        cells = new ArrayList<>();
        for (int i = 0; i < numberOfCells; i++) {
            cells.add(new Cell());
        }
        int indexChests = (int) (numberOfCells * Math.random());
        List<Chest> chests = new ArrayList<>();
        chests.add(new Chest(minTreasureCount, maxTreasureCount));
        cells.get(indexChests).setChests(chests);
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
