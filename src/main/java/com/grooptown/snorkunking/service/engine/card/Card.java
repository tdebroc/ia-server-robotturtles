package com.grooptown.snorkunking.service.engine.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.grooptown.snorkunking.service.engine.game.Game;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class Card {
    public abstract void play(Game game);

    public static String cardsToString(List<Card> cards) {
        return cards.stream().map(c -> c.getClass().getSimpleName().replace("Card", "")).collect(Collectors.joining( ", " ));
    }

    // Needed for JSON de-serialization
    public String getCardName() {
        return this.getClass().getSimpleName();
    }
}
