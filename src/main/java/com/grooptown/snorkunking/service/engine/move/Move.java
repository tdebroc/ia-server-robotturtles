package com.grooptown.snorkunking.service.engine.move;

import com.grooptown.snorkunking.service.engine.game.Game;

/**
 * Created by thibautdebroca on 08/11/2019.
 */
public abstract class Move {
    protected Game game;
    public void setGame(Game game) {
        this.game = game;
    }
    public abstract boolean isValidMove(String entry);
    public abstract void constructMoveFromEntry(String entry);
    public abstract void playMove();
    public abstract String entryQuestion();
}
