package com.grooptown.snorkunking.service.game.moves;

/**
 * Created by thibautdebroca on 02/12/2017.
 */
public class RecordMove {
    private String move;
    private int idPlayer;

    public RecordMove() {
    }

    public RecordMove(String move, int idPlayer) {
        this.move = move;
        this.idPlayer = idPlayer;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public int getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }
}
