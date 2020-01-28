package com.grooptown.snorkunking.service.engine.move;

import com.grooptown.snorkunking.service.engine.grid.EmptyPanel;
import com.grooptown.snorkunking.service.engine.grid.Panel;
import com.grooptown.snorkunking.service.engine.grid.RubyPanel;
import com.grooptown.snorkunking.service.engine.player.DirectionEnum;
import com.grooptown.snorkunking.service.engine.player.Player;
import com.grooptown.snorkunking.service.engine.player.Position;
import com.grooptown.snorkunking.service.engine.tile.Tile;
import com.grooptown.snorkunking.service.engine.tile.TileService;
import com.grooptown.snorkunking.service.engine.tile.WallTile;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static com.grooptown.snorkunking.service.engine.player.MovementService.directions;
import static com.grooptown.snorkunking.service.engine.player.MovementService.getNextPosition;

/**
 * Created by thibautdebroca on 08/11/2019.
 */
public class BuildWallMove extends Move {

    private Tile tileToBuild;

    private int line;

    private int column;

    @Override
    public boolean isValidMove(String entry) {
        if (!Pattern.compile("[Ice|Wa{0,1}l{2}] on [0-7]-[0-7]").matcher(entry).find()) {
            return false;
        }
        String[] entrySplit = entry.split(" ");
        tileToBuild = TileService.getTile(entrySplit[0]);
        if (!game.findCurrentPlayer().hasTile(tileToBuild)) {
            System.out.println("Vous n'avez pas de " + entrySplit[0]);
            return false;
        }
        line = Integer.parseInt(entrySplit[2].split("-")[0]);
        column = Integer.parseInt(entrySplit[2].split("-")[1]);

        if (!game.getGrid().getGrid()[line][column].getClass().equals(EmptyPanel.class) ) {
            System.out.println("Cette case est déjà occupé.");
            return false;
        }
        Position position = new Position(line, column);
        if (tileToBuild.getClass().equals(WallTile.class)
            && isBlockingRuby(position)) {
            return false;
        }
        return true;
    }

    private boolean isBlockingRuby(Position newWallPosition) {
        boolean isBlocked = false;
        for (Player player : game.getPlayers()) {
            Position playerPosition = game.getGrid().getPosition(player);
            Set<Position> visited = new HashSet<>();
            boolean hasAccessToRuby = hasAccessToRuby(playerPosition, newWallPosition, visited);
            if (!hasAccessToRuby) {
                isBlocked = true;
            }
        }
        return isBlocked;
    }


    private boolean hasAccessToRuby(Position currentPosition, Position newWallPosition, Set<Position> visited) {
        if (visited.contains(currentPosition)) {
            return false;
        }
        visited.add(currentPosition);
        if (game.getGrid().isOutOfBound(currentPosition)) {
            return false;
        }
        Class<? extends Panel> currentPanelType = game.getGrid().getPanel(currentPosition).getClass();
        if (currentPanelType.equals(RubyPanel.class)) {
            return true;
        }
        if (currentPanelType.equals(WallTile.class) || currentPosition.equals(newWallPosition)) {
            return false;
        }
        for (DirectionEnum direction : directions) {
            Position nextPosition = getNextPosition(currentPosition, direction);
            boolean hasAccessToRuby = hasAccessToRuby(nextPosition, newWallPosition, visited);
            if (hasAccessToRuby) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void constructMoveFromEntry(String entry) {

    }

    @Override
    public void playMove() {
        game.addMoveDescription(" - Player added a Wall of type " + tileToBuild.toAscii()
            + " in cell [" + line + "," + column + "] \n");
        game.getGrid().getGrid()[line][column] = tileToBuild;
        game.findCurrentPlayer().removeTile(tileToBuild);
    }

    @Override
    public String entryQuestion() {
        return "Which wall do you want to build and where ? (i.e.: 'Ice on 0-3' for line 0 and column 3, or 'Wall on 4-2')";
    }

    public Tile getTileToBuild() {
        return tileToBuild;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
