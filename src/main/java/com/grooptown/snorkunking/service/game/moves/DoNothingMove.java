package com.grooptown.snorkunking.service.game.moves;

import com.grooptown.snorkunking.service.game.Game;
import com.grooptown.snorkunking.service.game.Player;

/**
 * Created by thibautdebroca on 12/11/2017.
 */
public class DoNothingMove implements Move {
    @Override
    public boolean isValidMove(Game game, Player player) {
        return true;
    }

    @Override
    public void playMove(Game game, Player player) {

    }
}
