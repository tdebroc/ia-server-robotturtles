package com.grooptown.snorkunking.service.game;

import groovy.transform.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by thibautdebroca on 11/11/2017.
 */
@ToString
public class Player {

    public Player() {

    }

    private String uniqueID = UUID.randomUUID().toString();

    private String name;

    private int treasureCount = 0;

    private Integer caveIndex;

    private Integer levelIndex;

    private Integer cellIndex = 0;

    private List<Chest> chestsHolding = new ArrayList<>();

    public Integer getCaveIndex() {
        return caveIndex;
    }

    public void setCaveIndex(Integer caveIndex) {
        this.caveIndex = caveIndex;
    }

    public Integer getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(Integer levelIndex) {
        this.levelIndex = levelIndex;
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTreasureCount() {
        return treasureCount;
    }

    public void setTreasureCount(int treasureCount) {
        this.treasureCount = treasureCount;
    }

    public void addChest(Chest chest) {
        chestsHolding.add(chest);
    }

    public List<Chest> getChestsHolding() {
        return chestsHolding;
    }

    public void setChestsHolding(List<Chest> chestsHolding) {
        this.chestsHolding = chestsHolding;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (treasureCount != player.treasureCount) return false;
        if (uniqueID != null ? !uniqueID.equals(player.uniqueID) : player.uniqueID != null) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        if (caveIndex != null ? !caveIndex.equals(player.caveIndex) : player.caveIndex != null) return false;
        return levelIndex != null ? levelIndex.equals(player.levelIndex) : player.levelIndex == null;

    }

    @Override
    public int hashCode() {
        int result = uniqueID != null ? uniqueID.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + treasureCount;
        result = 31 * result + (caveIndex != null ? caveIndex.hashCode() : 0);
        result = 31 * result + (levelIndex != null ? levelIndex.hashCode() : 0);
        return result;
    }

    public void openChests() {
        int newTreasures = 0;
        for (Chest chest : getChestsHolding()) {
            newTreasures += chest.getTreasureCount();
        }
        setTreasureCount(getTreasureCount() + newTreasures);
        setChestsHolding(new ArrayList<>());
    }


    public Integer getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
    }


}
