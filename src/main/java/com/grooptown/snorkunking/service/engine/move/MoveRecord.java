package com.grooptown.snorkunking.service.engine.move;

import com.grooptown.snorkunking.service.engine.player.Position;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MoveRecord {
    private int turnNumber;
    private String title;
    private List<String> description = new LinkedList<>();
    private String playerName;
    private int numberOfCardFold;
    private int numberOfCardAddedToProgram;
    private List<String> cardsExecutedInProgram;
    private String wallAddedType;
    private int wallAddedLine;
    private int wallAddedColumn;
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

    public int getNumberOfCardAddedToProgram() {
        return numberOfCardAddedToProgram;
    }

    public void setNumberOfCardAddedToProgram(int numberOfCardAddedToProgram) {
        this.numberOfCardAddedToProgram = numberOfCardAddedToProgram;
    }

    public List<String> getCardsExecutedInProgram() {
        return cardsExecutedInProgram;
    }

    public void setCardsExecutedInProgram(List<String> cardsExecutedInProgram) {
        this.cardsExecutedInProgram = cardsExecutedInProgram;
    }

    public String getWallAddedType() {
        return wallAddedType;
    }

    public void setWallAddedType(String wallAddedType) {
        this.wallAddedType = wallAddedType;
    }

    public int getWallAddedLine() {
        return wallAddedLine;
    }

    public void setWallAddedLine(int wallAddedLine) {
        this.wallAddedLine = wallAddedLine;
    }

    public int getWallAddedColumn() {
        return wallAddedColumn;
    }

    public void setWallAddedColumn(int wallAddedColumn) {
        this.wallAddedColumn = wallAddedColumn;
    }
}
