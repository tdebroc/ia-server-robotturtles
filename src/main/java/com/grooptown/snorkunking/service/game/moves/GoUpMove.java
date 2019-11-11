package com.grooptown.snorkunking.service.game.moves;

import com.grooptown.snorkunking.service.game.Game;
import com.grooptown.snorkunking.service.game.Player;

/**
 * Created by thibautdebroca on 12/11/2017.
 */
public class GoUpMove implements Move {
    @Override
    public boolean isValidMove(Game game, Player player) {
        if (player.getCaveIndex() == null && player.getLevelIndex() == null) {
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
        if (player.getLevelIndex() == 0 && player.getCaveIndex() == 0) {
            player.setCaveIndex(null);
            player.setLevelIndex(null);
            if (player.getChestsHolding().size() > 0) {
                player.openChests();
            }
        } else if (player.getLevelIndex() == 0 ) {
            player.setCaveIndex(player.getCaveIndex() - 1);
            player.setLevelIndex(game.getCaves().get(player.getCaveIndex()).getLevels().size() - 1);
        } else {
            player.setLevelIndex(player.getLevelIndex() - 1);
        }
    }
}
