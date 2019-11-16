package com.grooptown.snorkunking.service.engine.card;

import com.grooptown.snorkunking.service.engine.game.Game;
import com.grooptown.snorkunking.service.engine.player.DirectionEnum;
import com.grooptown.snorkunking.service.engine.player.MovementService;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
public class YellowCard extends Card {
    @Override
    public void play(Game game) {
        DirectionEnum nextDirection = MovementService.getNextPosition(game.getCurrentPlayer().getDirection(), false);
        game.getCurrentPlayer().setDirection(nextDirection);
        game.addMoveDescription("New Direction of the turtle is " + nextDirection + " \n");
    }
}
