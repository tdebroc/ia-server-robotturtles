package com.grooptown.snorkunking.service.engine.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grooptown.snorkunking.service.engine.card.Card;
import com.grooptown.snorkunking.service.engine.card.CardDeck;
import com.grooptown.snorkunking.service.engine.card.CardService;
import com.grooptown.snorkunking.service.engine.game.Game;
import com.grooptown.snorkunking.service.engine.grid.Grid;
import com.grooptown.snorkunking.service.engine.grid.Panel;
import com.grooptown.snorkunking.service.engine.grid.PanelEnum;
import com.grooptown.snorkunking.service.engine.tile.IceTile;
import com.grooptown.snorkunking.service.engine.tile.Tile;
import com.grooptown.snorkunking.service.engine.tile.WallTile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.grooptown.snorkunking.service.engine.card.Card.cardsToString;
import static com.grooptown.snorkunking.service.engine.player.MovementService.getOppositeDirection;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
public class Player implements Panel {
    public static final int MAX_CARD_ALLOWED_IN_HAND = 5;
    private String playerName;
    private List<Tile> tiles = new ArrayList<>();
    private final List<Card> handCards = new ArrayList<>();
    private final List<Card> program = new ArrayList<>();
    private final List<Card> discarded = new ArrayList<>();
    private CardDeck cardDeck;
    private DirectionEnum direction = DirectionEnum.SOUTH;
    private Position initialPosition;
    private boolean rubyReached;
    public Player() {
        // For Jackson
    }

    public Player(String playerName) {
        this.playerName = playerName;
    }


    public void initPlayerTiles() {
        for (int j = 0; j < 3; j++) {
            tiles.add(new WallTile());
        }
        for (int j = 0; j < 2; j++) {
            tiles.add(new IceTile());
        }
    }

    // For Jackson serialization
    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Card> handCards() {
        return handCards;
    }



    @Override
    public String toAscii() {
        return " T" + playerName.substring(0, 2) + " ";
    }

    public void setCardDeck(CardDeck cardDeck) {
        this.cardDeck = cardDeck;
    }

    public void pickCardInDeck() {
        if (cardDeck.getCards().size() == 0) {
            Collections.shuffle(discarded);
            cardDeck.getCards().addAll(discarded);
            discarded.clear();
        }
        if (handCards.size() >= MAX_CARD_ALLOWED_IN_HAND) {
            throw new RuntimeException("Already " + MAX_CARD_ALLOWED_IN_HAND + " handCards ...");
        }
        handCards.add(cardDeck.getCards().pollFirst());
    }

    public void displayPlayer() {
        System.out.println("###### Player " + (playerName) + " id in Direction " + direction + " and has : ######");
        System.out.println("  - " + tiles.size() + " Tiles : " + tiles);
        System.out.println("  - " + handCards.size() + " Cards : " + cardsToString(handCards));
        System.out.println("  - Program is composed of " + program.size() + " Cards : " + cardsToString(program));
    }

    public void foldProgramCards() {
        Iterator<Card> iterator = program.iterator();
        for (; iterator.hasNext(); ) {
            Card card = iterator.next();
            addToDiscarded(card);
            iterator.remove();
        }
    }

    public boolean hasTile(Tile tile) {
        return getTiles().stream().anyMatch(t -> t.getClass().equals(tile.getClass()));
    }

    public void removeTile(Tile tile) {
        Iterator<Tile> tilesIt = tiles.iterator();
        while (tilesIt.hasNext()) {
            Tile currentTile = tilesIt.next();
            if (currentTile.getClass().equals(tile.getClass())) {
                tilesIt.remove();
                return;
            }
        }
        System.err.println("Error, should have tile to be removed...");
    }

    public void addCardsToProgram(List<Card> newCards) {
        program.addAll(newCards);
    }

    public List<Card> program() {
        return program;
    }

    public DirectionEnum getDirection() {
        return direction;
    }

    public void setDirection(DirectionEnum direction) {
        this.direction = direction;
    }

    public void setInitialPosition(Position initialPosition) {
        setDirection(DirectionEnum.SOUTH);
        this.initialPosition = initialPosition;
    }

    public void backToInitialPosition(Grid grid) {
        moveTo(initialPosition, grid);
        setDirection(DirectionEnum.SOUTH);
    }

    public void moveTo(Position nextPosition, Grid grid) {
        Position oldPosition = grid.getPosition(this);
        grid.makeCellEmpty(oldPosition);
        grid.placePlayer(nextPosition, this);
    }

    public void touchTurtle(Game game) {
        backToInitialPosition(game.getGrid());
    }

    public void touchLaser(Game game) {
        if (game.getPlayers().size() > 2) {
            backToInitialPosition(game.getGrid());
        } else {
            reverseDirection();
        }
    }

    public void reverseDirection() {
        DirectionEnum oppositeDirection = getOppositeDirection(this.getDirection());
        this.setDirection(oppositeDirection);
    }

    public boolean isRubyReached() {
        return rubyReached;
    }

    public void setRubyReached(boolean rubyReached) {
        this.rubyReached = rubyReached;
    }

    @Override
    public PanelEnum getPanelName() {
        return PanelEnum.PLAYER;
    }

    // Needed For JSON Object.
    public String getPlayerName() {
        return playerName;
    }

    public void removeCardsFromHand(List<Card> cardsToAdd) {
        CardService.removeCardsFromHand(this.handCards(), cardsToAdd);
    }

    public Position getInitialPosition() {
        return initialPosition;
    }

    @JsonIgnore
    public PlayerSecret getSecrets() {
        return new PlayerSecret(handCards, program);
    }

    public void clearSecrets() {
        this.handCards.clear();
        this.program.clear();
    }

    public void addToDiscarded(Card card) {
        discarded.add(card);
    }
}
