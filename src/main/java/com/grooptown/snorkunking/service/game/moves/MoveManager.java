package com.grooptown.snorkunking.service.game.moves;

import com.grooptown.snorkunking.service.game.Game;
import com.grooptown.snorkunking.service.game.Player;
import com.grooptown.snorkunking.service.game.moves.*;

import java.util.Scanner;

/**
 * Created by thibautdebroca on 12/11/2017.
 */
public class MoveManager {

    static Scanner scan = new Scanner(System.in);

    public static Move askNextMove(Game game, Player player) {
        Move move;
        do {
            move = getNextMoveFromCommandLine(player);
        } while (!isValidMove(move, game, player));
        return move;
    }

    public static Move getNextMoveFromCommandLine(Player player) {
        System.out.println("\nPlayer " + player.getName() + " it's your turn. Please chose a move by entering a number:");
        System.out.println("1. Go Up.");
        System.out.println("2. Go Down.");
        System.out.println("3. Pick Treasure. (If you want another than the first one, precise the number of the treasure from 1 to N. i.e.: '3 2' to take the 2sd treasure).");
        System.out.println("4. Do Nothing.");
        String line = scan.nextLine();
        return getNextMove(line);
    }

    public static Move getNextMove(String line) {
        if (line.length() == 0) {
            System.err.println("Invalid input");
            return null;
        }
        int moveIndex;
        try {
            moveIndex = Integer.parseInt(line.split(" ")[0]);
        } catch (NumberFormatException e) {
            System.err.println("Please enter a valid number");
            return null;
        }
        if (moveIndex == 1) {
            return new GoUpMove();
        } else if (moveIndex == 2) {
            return new GoDownMove();
        } else if (moveIndex == 3) {
            if (line.split(" ").length == 1) {
                return new PickTreasureMove(0);
            }
            try {
                int treasureIndex = Integer.parseInt(line.split(" ")[1]);
                return new PickTreasureMove(treasureIndex);
            } catch (NumberFormatException e) {
                System.err.println("Please enter a valid number");
                return null;
            }
        } else if (moveIndex == 4) {
            return new GoLeftMove();
        } else if (moveIndex == 5) {
            return new GoRightMove();
        } else if (moveIndex == 6) {
            return new DoNothingMove();
        } else {
            System.err.println("Please enter a valid number");
            return null;
        }
    }

    public static boolean isValidMove(Move move, Game game, Player player) {
        if (move == null) return false;
        return move.isValidMove(game, player);
    }
}
