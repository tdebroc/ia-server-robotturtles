package com.grooptown.snorkunking.service.engine.card;

import java.util.*;
import java.util.stream.Collectors;

import static com.grooptown.snorkunking.service.engine.player.Player.MAX_CARD_ALLOWED_IN_HAND;

/**
 * Created by thibautdebroca on 08/11/2019.
 */
public class CardService {

    private final static Map<Character, Card> charToCards = new HashMap<>();
    static {
        charToCards.put('B', new BlueCard());
        charToCards.put('L', new LaserCard());
        charToCards.put('P', new PurpleCard());
        charToCards.put('Y', new YellowCard());
    }

    public static List<Card> getNewCards(String cards) {
        return cards
                .chars()
                .mapToObj(c -> (char) c)
                .map(c -> charToCards.get(c))
                .collect(Collectors.toList());
    }

    public static boolean isValidCardsEntry(String cards) {
        return cards.length() >= 1
                && cards.length() <= MAX_CARD_ALLOWED_IN_HAND
                && CardService.isValidCardSet(cards);

    }

    private static boolean isValidCardSet(String cards) {
        for (int i = 0; i < cards.length(); i++) {
            if (!isValidCard(cards.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidCard(char c) {
        return charToCards.containsKey(c);
    }

    public static boolean hasEnoughCards(String entryToVerify, List<Card> setOfCards) {
        Set<Card> handCards = new HashSet<>(setOfCards);
        for (Card card : CardService.getNewCards(entryToVerify)) {
            boolean found = false;
            Iterator<Card> iterator = handCards.iterator();
            while (iterator.hasNext()) {
                Card nextCard = iterator.next();
                if (nextCard.getClass().equals(card.getClass())) {
                    found = true;
                    iterator.remove();
                    break;
                }
            }
            if (!found) {
                System.out.println("You don't have enough " + card.getClass().getSimpleName());
                return false;
            }
        }
        return true;
    }

    public static void removeCardsFromHand(List<Card> handOfCard,
                                           List<Card> cardToRemove) {
        for (Card card : cardToRemove) {
            boolean found = false;
            Iterator<Card> iterator = handOfCard.iterator();
            while (iterator.hasNext()) {
                Card nextCard = iterator.next();
                if (nextCard.getClass().equals(card.getClass())) {
                    found = true;
                    iterator.remove();
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("Can't Remove this card : " + card.getClass().getSimpleName());
            }
        }
    }
}
