package com.grooptown.snorkunking.service.engine.card;

import com.grooptown.snorkunking.service.engine.game.Game;
import com.grooptown.snorkunking.service.engine.grid.RubyPanel;
import com.grooptown.snorkunking.service.engine.player.DirectionEnum;
import com.grooptown.snorkunking.service.engine.player.Player;
import com.grooptown.snorkunking.service.engine.player.Position;
import com.grooptown.snorkunking.service.engine.tile.IceTile;
import com.grooptown.snorkunking.service.engine.tile.WallTile;

import static com.grooptown.snorkunking.service.engine.player.MovementService.getNextPosition;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
public class LaserCard extends Card {
    @Override
    public void play(Game game) {
        Player currentPlayer = game.getCurrentPlayer();
        Position position = game.getGrid().getPosition(currentPlayer);
        DirectionEnum laserDirection = currentPlayer.getDirection();
        Position nextLaserPosition = getNextPosition(position, laserDirection);
        while (!game.getGrid().isOutOfBound(nextLaserPosition)) {
            if (game.getGrid().getPanel(nextLaserPosition).getClass().equals(IceTile.class)) {
                game.getGrid().makeCellEmpty(nextLaserPosition);
                return;
            }
            if (game.getGrid().getPanel(nextLaserPosition).getClass().equals(WallTile.class)) {
                return;
            }
            if (game.getGrid().getPanel(nextLaserPosition).getClass().equals(Player.class)) {
                Player touchedPlayer = (Player) game.getGrid().getPanel(nextLaserPosition);
                touchedPlayer.touchLaser(game);
                return;
            }
            if (game.getGrid().getPanel(nextLaserPosition).getClass().equals(RubyPanel.class)) {
                game.getCurrentPlayer().touchLaser(game);
                return;
            }
            nextLaserPosition = getNextPosition(nextLaserPosition, laserDirection);
        }
    }
}
