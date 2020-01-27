package com.grooptown.snorkunking.service.engine.move;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MoveRecord {
    private int turnNumber;
    private String title;
    private List<String> description = new LinkedList<>();
    private String playerName;
    private int numberOfCardFold;
    private Date date;

    public MoveRecord() {
        this.date = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getNumberOfCardFold() {
        return numberOfCardFold;
    }

    public void setNumberOfCardFold(int numberOfCardFold) {
        this.numberOfCardFold = numberOfCardFold;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
