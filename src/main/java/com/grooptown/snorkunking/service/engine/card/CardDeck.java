package com.grooptown.snorkunking.service.engine.card;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
public class CardDeck {
    public LinkedList<Card> getCards() {
        return cards;
    }

    private LinkedList<Card> cards;

    public CardDeck() {
        buildDefaultDeck();
    }

    public void buildDefaultDeck() {
        cards = new LinkedList<>();
        for (int i = 0; i < 18; i++) {
            cards.add(new BlueCard());
        }
        for (int i = 0; i < 8; i++) {
            cards.add(new YellowCard());
        }
        for (int i = 0; i < 8; i++) {
            cards.add(new PurpleCard());
        }
        for (int i = 0; i < 3; i++) {
            cards.add(new LaserCard());
        }
        Collections.shuffle(cards);
    }

}
