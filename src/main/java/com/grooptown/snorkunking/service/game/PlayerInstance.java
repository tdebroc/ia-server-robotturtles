package com.grooptown.snorkunking.service.game;

import groovy.transform.ToString;

import java.util.Date;


/**
 * Created by thibautdebroca on 26/11/2017.
 */
@ToString
public class PlayerInstance {
    private int idGame;
    private String UUID;
    private int idPlayer;
    private Date timeStart;
    private PlayerInstance() {}
    public PlayerInstance(int idGame, int idPlayer, String userId) {
        this.idPlayer = idPlayer;
        this.idGame = idGame;
        this.UUID = userId;
        timeStart = new Date();
    }

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Player getPlayerFromInstance(Game game) {
        return game.getPlayers().get(getIdPlayer());
    }
}
