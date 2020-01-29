package com.grooptown.snorkunking.service.engine.move;

import com.grooptown.snorkunking.service.engine.card.Card;

import java.util.List;

/**
 * Created by thibautdebroca on 08/11/2019.
 */
public class ExecuteMove extends Move {
    private List<String> cardsInProgram;  // e.g. ["B", "J", "L"]

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
            this.cardsInProgram.add(card.getCardName().substring(0, 1));  // Get first character of the string as a string
            card.play(game);
            if (game.findCurrentPlayer().isRubyReached()) {
                break;
            }
        }
        game.findCurrentPlayer().foldProgramCards();

    }

    public List<String> getCardsInProgram() {
        // Assuming getCardsInProgram() will be called after playMove() has been executed
        return this.cardsInProgram;
    }

    @Override
    public String entryQuestion() {
        return null;
    }
}
