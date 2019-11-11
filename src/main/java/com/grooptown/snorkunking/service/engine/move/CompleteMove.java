package com.grooptown.snorkunking.service.engine.move;

import com.grooptown.snorkunking.service.engine.card.Card;
import com.grooptown.snorkunking.service.engine.card.CardService;

import java.util.List;

import static com.grooptown.snorkunking.service.engine.player.Player.MAX_CARD_ALLOWED_IN_HAND;

/**
 * Created by thibautdebroca on 08/11/2019.
 */
public class CompleteMove extends Move {
    private List<Card> cardsToAdd;

    @Override
    public boolean isValidMove(String entry) {
        if (!CardService.isValidCardsEntry(entry)) {
            System.out.println("Entry is not Valid");
            return false;
        }
        if (game.getCurrentPlayer().getProgram().size() + entry.length() > MAX_CARD_ALLOWED_IN_HAND) {
            System.out.println("You'll have too many Card in your Program !");
            return false;
        }
        return CardService.hasEnoughCards(entry, game.getCurrentPlayer().getHandCards());
    }

    @Override
    public void constructMoveFromEntry(String entry) {
        cardsToAdd = CardService.getNewCards(entry);
    }

    @Override
    public void playMove() {
        game.getCurrentPlayer().removeCardsFromHand(cardsToAdd);
        game.getCurrentPlayer().addCardsToProgram(cardsToAdd);
    }

    @Override
    public String entryQuestion() {
        return "What cards (1 to " + MAX_CARD_ALLOWED_IN_HAND
                + ") do you want to add ? ( i.e.: BBLYP (for Blue, Blue, Laser, Yellow, Purple)";
    }

    public List<Card> getCardsToAdd() {
        return cardsToAdd;
    }
}
