package com.grooptown.snorkunking.service.engine.move;

import com.grooptown.snorkunking.service.engine.card.Card;

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
        for (Card card : game.findCurrentPlayer().program()) {
            game.addMoveDescription(" - Playing " + card.getCardName() + "\n");
            card.play(game);
            if (game.findCurrentPlayer().isRubyReached()) {
                break;
            }
        }
        game.findCurrentPlayer().foldProgramCards();

    }

    @Override
    public String entryQuestion() {
        return null;
    }
}
