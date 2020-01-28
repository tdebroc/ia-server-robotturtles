package com.grooptown.snorkunking.service.engine.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grooptown.snorkunking.service.engine.card.Card;
import com.grooptown.snorkunking.service.engine.card.CardDeck;
import com.grooptown.snorkunking.service.engine.card.CardService;
import com.grooptown.snorkunking.service.engine.grid.Grid;
import com.grooptown.snorkunking.service.engine.grid.RubyPanel;
import com.grooptown.snorkunking.service.engine.move.*;
import com.grooptown.snorkunking.service.engine.player.Player;
import com.grooptown.snorkunking.service.engine.player.Position;
import com.grooptown.snorkunking.service.engine.tile.WallTile;

import java.util.*;

import static com.grooptown.snorkunking.service.engine.card.Card.cardsToString;
import static com.grooptown.snorkunking.service.engine.player.Player.MAX_CARD_ALLOWED_IN_HAND;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
public class Game {
    private int idGame;

    public static final int MAX_NUM_PLAYER = 4;

    private int currentPlayerIndex = 0;

    private int turnCount = 0;

    private List<Player> players = new ArrayList<>();

    private List<Player> leaderBoard = new ArrayList<>();

    private boolean finished = false;

    private boolean started = false;

    private LinkedList<MoveRecord> moveRecords = new LinkedList<>();

    private LinkedList<Grid> gridHistory = new LinkedList<>();

    private Grid grid;

    public Game() {
        // Needed For Jackson Deserialization.
    }
    public Game(int idGame) {
        this.idGame = idGame;
    }

    public void play() {
        addPlayers(2);
        while (!finished) {
            displayGame();
            AllMove allMove = getMoveFromCommandLine();
            playMove(allMove);
        }
    }

    //==================================================================================================================
    // Ask AllMove
    //==================================================================================================================

    private Move[] getNewPossiblesMoves() {
        return new Move[]{null, new CompleteMove(), new BuildWallMove(), new ExecuteMove()};
    }

    public AllMove getMoveFromString(String entry) {
        Move[] possibleMoves = getNewPossiblesMoves();
        AllMove allMove = new AllMove();
        String[] entrySplit = entry.split(";", -1);
        if (entrySplit.length != 3) {
            return null;
        }
        int principalMoveIndex = Integer.parseInt(entrySplit[0]);
        allMove.setMove(possibleMoves[principalMoveIndex]);
        allMove.getMove().setGame(this);
        String additionalInfo = entrySplit[1];
        if (!allMove.getMove().isValidMove(additionalInfo)) {
            return null;
        }
        allMove.getMove().constructMoveFromEntry(additionalInfo);
        String cardsToFold = entrySplit[2];
        if (!allMove.areCardToFoldValid(cardsToFold, this)) {
            return null;
        }
        allMove.setCardToFold(CardService.getNewCards(cardsToFold));
        return allMove;
    }


    private AllMove getMoveFromCommandLine() {
        Move[] possibleMoves = getNewPossiblesMoves();
        AllMove allMove = new AllMove();
        System.out.println("Player " + (currentPlayerIndex + 1) + " it's your Turn !");
        int principalMove = 0;
        boolean inputMismatchException;
        do {
            inputMismatchException = false;
            try {
                System.out.println("What do you want to do ? (1 => Complete Program, 2 => Build Wall, 3 => Execute Program)");
                principalMove = new Scanner(System.in).nextInt();
            } catch (InputMismatchException e) {
                inputMismatchException = true;
            }
        } while (inputMismatchException || principalMove < 1 || principalMove > 3);
        allMove.setMove(possibleMoves[principalMove]);
        allMove.getMove().setGame(this);

        if (!allMove.getMove().getClass().equals(ExecuteMove.class)) {
            String entry;
            do {
                System.out.println(allMove.getMove().entryQuestion());
                entry = new Scanner(System.in).nextLine();
            } while (!allMove.getMove().isValidMove(entry));
            allMove.getMove().constructMoveFromEntry(entry);
        }

        String cardToFold;

        List<Card> playerHands = new LinkedList<>(this.findCurrentPlayer().handCards());
        if (allMove.getMove().getClass().equals(CompleteMove.class)) {
            CardService.removeCardsFromHand(playerHands, ((CompleteMove) (allMove.getMove())).getCardsToAdd());
        }
        do {
            System.out.println("Enter cards that you want to fold. If you don't want to fold card, press ENTER. Your remaining cards are : " + cardsToString(playerHands));
            cardToFold = new Scanner(System.in).nextLine().toUpperCase();
        } while (allMove.areCardToFoldValid(cardToFold, this));
        allMove.setCardToFold(CardService.getNewCards(cardToFold));

        return allMove;
    }


    //==================================================================================================================
    // Play AllMove
    //==================================================================================================================

    public void playMove(AllMove allMove) {
        addToGridHistory();
        addToMoveRecord(allMove);
        allMove.getMove().playMove();
        foldAndPickNewCards(allMove);
        checkIfGameIsFinished();
        selectNextPlayer();
        turnCount++;
    }

    private void addToMoveRecord(AllMove allMove) {
        MoveRecord moveRecord = new MoveRecord();
        moveRecords.add(moveRecord);
        moveRecord.setPlayerName(findCurrentPlayer().getPlayerName());
        moveRecord.setTitle(allMove.getMove().getClass().getSimpleName());
        moveRecord.setTurnNumber(turnCount);
        if (allMove.getMove().getClass().equals(BuildWallMove.class)) {
            BuildWallMove move = (BuildWallMove) allMove.getMove();
            moveRecord.setWallAddedType(move.getTileToBuild().toString());
            moveRecord.setWallAddedLine(move.getLine());
            moveRecord.setWallAddedColumn(move.getColumn());
        } else if (allMove.getMove().getClass().equals(CompleteMove.class)) {
            CompleteMove move = (CompleteMove) allMove.getMove();
            moveRecord.setNumberOfCardAddedToProgram(move.getCardsToAdd().size());
        }
    }

    private void addToGridHistory() {
        gridHistory.add(grid.cloneGrid());
    }

    private void selectNextPlayer() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (findCurrentPlayer().isRubyReached());
    }

    private void foldAndPickNewCards(AllMove allMove) {
        for (Card card : findCurrentPlayer().handCards()) {
            findCurrentPlayer().addToDiscarded(card);
        }
        CardService.removeCardsFromHand(findCurrentPlayer().handCards(), allMove.getCardToFold());
        while (findCurrentPlayer().handCards().size() < MAX_CARD_ALLOWED_IN_HAND) {
            findCurrentPlayer().pickCardInDeck();
        }
        moveRecords.getLast().setNumberOfCardFold(allMove.getCardToFold().size());
    }

    private void checkIfGameIsFinished() {
        if (isGameFinished()) {
            finished = true;
        }
    }

    private boolean isGameFinished() {
        long countPlayerWithRubyReach = players.stream().filter(Player::isRubyReached).count();
        return countPlayerWithRubyReach >= players.size() - 1;
    }

    //==================================================================================================================
    // Init Game
    //==================================================================================================================
    private void displayGame() {
        grid.displayGrid();
        for (Player player : players) {
            player.displayPlayer();
        }
    }

    private void initGame() {
        initGrid();
        initDecks();
        pickCards();
        initCurrentPlayerIndex();
    }

    private void initCurrentPlayerIndex() {
        currentPlayerIndex = (int) (Math.random() * players.size());
        currentPlayerIndex = 0;
    }

    private void initDecks() {
        for (Player player : players) {
            player.setCardDeck(new CardDeck());
        }
    }

    private void pickCards() {
        for (Player player : players) {
            for (int i = 0; i < MAX_CARD_ALLOWED_IN_HAND; i++) {
                player.pickCardInDeck();
            }
        }
    }

    private void addPlayers(int playerCount) {
        for (int i = 0; i < playerCount; i++) {
            players.add(new Player("t" + i));
        }
    }

    private void initGrid() {
        grid = new Grid(8);
        Position[] initialPositions = new Position[players.size()];
        int middleOfLineIndex = grid.getGrid()[0].length / 2 - 1;
        int lastLineIndex = grid.getGrid().length - 1;
        if (players.size() == 2) {
            initialPositions[0] = new Position(0, 1);
            initialPositions[1] = new Position(0, grid.getGrid()[0].length - 3);
            grid.getGrid()[lastLineIndex][middleOfLineIndex] = new RubyPanel();
            buildRightWall();
        } else if (players.size() == 3) {
            initialPositions[0] = new Position(0, 0);
            initialPositions[1] = new Position(0, middleOfLineIndex);
            initialPositions[2] = new Position(0, grid.getGrid()[0].length - 2);
            grid.getGrid()[lastLineIndex][0] = new RubyPanel();
            grid.getGrid()[lastLineIndex][middleOfLineIndex] = new RubyPanel();
            grid.getGrid()[lastLineIndex][grid.getGrid()[0].length - 2] = new RubyPanel();
            buildRightWall();
        } else if (players.size() == 4) {
            initialPositions[0] = new Position(0, 0);
            initialPositions[1] = new Position(0, 2);
            initialPositions[2] = new Position(0, grid.getGrid()[0].length - 3);
            initialPositions[3] = new Position(0, grid.getGrid()[0].length - 1);
            grid.getGrid()[lastLineIndex][1] = new RubyPanel();
            grid.getGrid()[lastLineIndex][grid.getGrid()[0].length - 2] = new RubyPanel();
        } else {
            throw new RuntimeException("Only 1 to 4 players please.");
        }
        for (int i = 0; i < initialPositions.length; i++) {
            players.get(i).setInitialPosition(initialPositions[i]);
            grid.placePlayer(initialPositions[i], players.get(i));
        }
    }

    private void buildRightWall() {
        for (int j = 0; j < grid.getGrid().length; j++) {
            grid.getGrid()[j][grid.getGrid()[j].length - 1] = new WallTile();
        }
    }

    public Player findCurrentPlayer() {
        return players.size() > 0 ? players.get(currentPlayerIndex) : null;
    }

        public Grid getGrid() {
        return grid;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void addPlayer(String playerName) {
        Player player = new Player(playerName);
        player.initPlayerTiles();
        players.add(player);
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void startGame() {
        initGame();
        setStarted(true);
    }

    // Here for Jackson Deserialization
    public void setCurrentIdPlayerTurn(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public int getCurrentIdPlayerTurn() {
        return currentPlayerIndex;
    }

    public int getIdGame() {
        return idGame;
    }

    // Here for Jackson Deserialization
    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public String asJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public void setLeaderBoard(List<Player> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    public List<Player> getLeaderBoard() {
        return leaderBoard;
    }

    public void addMoveDescription(String moveDescription) {
        moveRecords.getLast().getDescription().add(moveDescription);
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public LinkedList<Grid> getGridHistory() {
        return gridHistory;
    }

    // Here for Jackson Deserialization
    public void setGridHistory(LinkedList<Grid> gridHistory) {
        this.gridHistory = gridHistory;
    }

    // Here for Jackson Deserialization
    public void setMoveRecords(LinkedList<MoveRecord> moveRecords) {
        this.moveRecords = moveRecords;
    }

    public LinkedList<MoveRecord> getMoveRecords() {
        return moveRecords;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public int getTurnCount() {
        return turnCount;
    }
}
