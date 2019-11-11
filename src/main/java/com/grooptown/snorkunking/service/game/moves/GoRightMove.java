package com.grooptown.snorkunking.service.game.moves;

import com.grooptown.snorkunking.service.game.Game;
import com.grooptown.snorkunking.service.game.Player;

/**
 * Created by thibautdebroca on 12/11/2017.
 */
public class GoRightMove implements Move {
    @Override
    public boolean isValidMove(Game game, Player player) {
        if (player.getCellIndex() >= game.getCaveWidth() - 1) {
            return false;
        }
        return true;
    }

    @Override
    public void playMove(Game game, Player player) {
        game.getCurrentStage().removeOxygen(1 + player.getChestsHolding().size());
        if (game.getCurrentStage().getOxygen() < 0) {
            return;
        }
        player.setCellIndex(
            player.getCellIndex() + 1
        );
    }
}
