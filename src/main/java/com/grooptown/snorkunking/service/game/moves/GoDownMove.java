package com.grooptown.snorkunking.service.game.moves;

import com.grooptown.snorkunking.service.game.Game;
import com.grooptown.snorkunking.service.game.Player;

/**
 * Created by thibautdebroca on 12/11/2017.
 */
public class GoDownMove implements Move {
    @Override
    public boolean isValidMove(Game game, Player player) {
        if (player.getCaveIndex() == game.getLastCaveIndex()
                && player.getLevelIndex() == game.getCaves().get(game.getLastCaveIndex()).getLevels().size() - 1) {
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
        if (player.getLevelIndex() == null && player.getCaveIndex() == null) {
            player.setLevelIndex(0);
            player.setCaveIndex(0);
        } else if (player.getLevelIndex() == game.getCaves().get(player.getCaveIndex()).findLastLevelIndex()) {
            player.setLevelIndex(0);
            player.setCaveIndex(player.getCaveIndex() + 1);
        } else {
            player.setLevelIndex(player.getLevelIndex() + 1);
        }

    }
}
