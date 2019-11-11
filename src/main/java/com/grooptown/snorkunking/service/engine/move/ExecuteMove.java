package com.grooptown.snorkunking.service.engine.move;

import com.grooptown.snorkunking.service.engine.card.Card;

import java.util.LinkedList;

/**
 * Created by thibautdebroca on 08/11/2019.
 */
public class ExecuteMove extends Move {
    @Override
    public boolean isValidMove(String entry) {
        return entry.isEmpty();
    }

    @Override
    public void constructMoveFromEntry(String entry) {
    }

    @Override
    public void playMove() {
        for (Card card : game.getCurrentPlayer().getProgram()) {
            if (!game.getCurrentPlayer().isRubyReached()) {
                card.play(game);
            } else {
                game.getLeaderBoard().add(game.getCurrentPlayer());
                break;
            }
        }
        game.getCurrentPlayer().foldProgramCards();

    }

    @Override
    public String entryQuestion() {
        return null;
    }
}
