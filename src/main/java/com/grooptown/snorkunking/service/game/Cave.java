package com.grooptown.snorkunking.service.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibautdebroca on 11/11/2017.
 */
public class Cave {

    private String name;

    private List<Level> levels;


    public Cave() {

    }

    public Cave(String name, int levelCountMin, int levelCountMax,
                int minTreasureCount, int maxTreasureCount, int caveWidth) {
        this.name = name;
        levels = new ArrayList<>();
        int levelCount = (int) (Math.random() * (levelCountMax - levelCountMin)) + levelCountMin;
        for (int i = 0; i < levelCount; i++) {
            levels.add(new Level(minTreasureCount, maxTreasureCount, caveWidth));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public int findLastLevelIndex() {
        return levels.size() - 1;
    }

}
