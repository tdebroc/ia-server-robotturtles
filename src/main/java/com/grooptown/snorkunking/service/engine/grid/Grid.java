package com.grooptown.snorkunking.service.engine.grid;

import com.grooptown.snorkunking.service.engine.player.Player;
import com.grooptown.snorkunking.service.engine.player.Position;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
public class Grid {
    private Panel[][] grid;
    public Grid() {
        // JAckson Serialization.
    }
    public Grid(int size) {
        grid = new Panel[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new EmptyPanel();
            }
        }
    }

    public void setGrid(Panel[][] grid) {
        this.grid = grid;
    }

    public Panel[][] getGrid() {
        return grid;
    }

    public void displayGrid() {
        System.out.print("   ");
        for (int i = 0; i < this.getGrid()[0].length; i++) {
            System.out.print(" " + i + "  ");
        }
        System.out.println();
        for (int i = 0; i < this.getGrid().length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < this.getGrid()[0].length; j++) {
                System.out.print(this.getGrid()[i][j].toAscii() + '|');
            }
            System.out.println();
        }
    }

    public void placePlayer(Position position, Player player) {
        grid[position.getLine()][position.getColumn()] = player;
    }

    public void makeCellEmpty(Position position) {
        grid[position.getLine()][position.getColumn()] = new EmptyPanel();
    }

    public Position getPosition(Player player) {
        for (int line = 0; line < grid.length; line++) {
            for (int column = 0; column < grid[0].length; column++) {
                if (grid[line][column].equals(player)) {
                    return new Position(line, column);
                }
            }
        }
        throw new RuntimeException("Didn't find the Player : " + player);
    }

    public boolean isOutOfBound(Position position) {
        return position.getColumn() < 0
                || position.getLine() < 0
                || position.getLine() >= getGrid().length
                || position.getColumn() >= getGrid()[0].length;
    }

    public Panel getPanel(Position position) {
        return grid[position.getLine()][position.getColumn()];
    }

    public Grid cloneGrid() {
        Grid newGrid = new Grid(grid.length);
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, newGrid.getGrid()[i], 0, grid[0].length);
        }
        return newGrid;
    }
}
