package com.grooptown.snorkunking.service.game;

import com.grooptown.snorkunking.service.game.moves.Move;
import com.grooptown.snorkunking.service.game.moves.MoveManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by thibautdebroca on 11/11/2017.
 */
public class Stage {

    private int oxygen;

    private int turn = 0;

    private int currentIdPlayerTurn = -1;

    List<Player> playersLeftToPlay = new ArrayList<>();

    public void initStage(Game game) {
        playersLeftToPlay = new ArrayList<>();
        putPlayersAtSurface(game);
        prepareMove(game);
    }

    public void prepareMove(Game game) {
        if (playersLeftToPlay.size() == 0) {
            playersLeftToPlay.addAll(game.getPlayers());
            turn++;
        }
        pickAndSetNextPlayerId(game);
        if (oxygen <= 0) {
            endStage(game);
        }
    }

    public void playStage(Game game) {
        initStage(game);
        while (oxygen > 0) {
            Player player = pickNextPlayer();
            game.displayGame();
            Move move = MoveManager.askNextMove(game, player);
            move.playMove(game, player);
            if (playersLeftToPlay.size() == 0) {
                playersLeftToPlay.addAll(game.getPlayers());
                turn++;
            }
        }
        endStage(game);
    }

    public void endStage(Game game) {
        makeChestFolds(game);
        calculateScore(game);
        removeLevelsWithNoChests(game);
        game.afterStage();
    }

    private void removeLevelsWithNoChests(Game game) {
        for (Cave cave : game.getCaves()) {
            List<Level> levels = cave.getLevels();
            Iterator<Level> i = levels.iterator();
            while (i.hasNext()) {
                Level level = i.next();
                boolean chestFound = false;
                for (int cellIndex = 0; cellIndex < level.getCells().size() && !chestFound; cellIndex++) {
                    if (level.getCells().get(cellIndex).getChests().size() > 0 ) {
                        chestFound = true;
                    }
                }
                if (!chestFound) {
                    i.remove();
                }
            }
        }
        Iterator<Cave> i = game.getCaves().iterator();
        while (i.hasNext()) {
            Cave cave = i.next();
            if (cave.getLevels().size() == 0) {
                i.remove();
            }
        }
    }

    private void putPlayersAtSurface(Game game) {
        for (Player player : game.getPlayers()) {
            player.setLevelIndex(null);
            player.setCaveIndex(null);
            player.setCellIndex(0);
        }
    }

    private void calculateScore(Game game) {
        for (Player player : game.getPlayers()) {
            player.openChests();
        }
    }

    private void makeChestFolds(Game game) {
        for (Player player : game.getPlayers()) {
            if (player.getCaveIndex() != null && player.getLevelIndex() != null) {
                game.getLastLevel().getCells().get(player.getCellIndex()).
                            getChests().addAll(player.getChestsHolding());
                player.setChestsHolding(new ArrayList<>());
            }
        }
    }


    public int pickAndSetNextPlayerId(Game game) {
        Player nextPlayer = pickNextPlayer();
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (nextPlayer.equals(game.getPlayers().get(i))) {
                currentIdPlayerTurn = i;
                return currentIdPlayerTurn;
            }
        }
        return -1;
    }

    public Player pickNextPlayer() {
        List<Player> playerAtLowestLevel = new ArrayList<>();
        Integer lowestLevel = null;
        Integer lowestCave = null;
        for (Player player : playersLeftToPlay) {
            if (player.getCaveIndex() == lowestCave
                    && lowestLevel == player.getLevelIndex()) {
                playerAtLowestLevel.add(player);
            }
            if (        player.getCaveIndex() != null && lowestCave == null
                    ||
                        player.getCaveIndex() != null && lowestCave != null &&
                        player.getCaveIndex() > lowestCave
                    ||
                        player.getCaveIndex() != null && lowestCave != null &&
                        player.getCaveIndex() == lowestCave &&
                        player.getLevelIndex() > lowestLevel
                    ) {
                playerAtLowestLevel = new ArrayList<>();
                playerAtLowestLevel.add(player);
                lowestLevel = player.getLevelIndex();
                lowestCave = player.getCaveIndex();
            }
        }
        Player player = playerAtLowestLevel.get((int) (Math.random() * playerAtLowestLevel.size()));
        playersLeftToPlay.remove(player);
        return player;
    }

    public Stage(int oxygen) {
        this.oxygen = oxygen;
    }

    public Stage() {
    }

    public int getOxygen() {
        return oxygen;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void removeOxygen(int oxygen) {
        this.oxygen -= oxygen;
    }

    public int getCurrentIdPlayerTurn() {
        return currentIdPlayerTurn;
    }
    public void setCurrentIdPlayerTurn(int currentIdPlayerTurn) {
        currentIdPlayerTurn = currentIdPlayerTurn;
    }
}
