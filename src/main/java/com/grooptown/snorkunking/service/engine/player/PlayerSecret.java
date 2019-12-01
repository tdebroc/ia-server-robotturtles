package com.grooptown.snorkunking.service.engine.player;

import com.grooptown.snorkunking.service.engine.card.Card;

import java.util.List;

public class PlayerSecret {
    private final List<Card> handCards;
    private final List<Card> program;

    public PlayerSecret(List<Card> handCards, List<Card> program) {
        this.handCards = handCards;
        this.program = program;
    }

    public List<Card> getProgram() {
        return program;
    }

    public List<Card> getHandCards() {
        return handCards;
    }
}
