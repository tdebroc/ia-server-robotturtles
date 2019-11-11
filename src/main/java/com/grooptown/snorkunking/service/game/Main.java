package com.grooptown.snorkunking.service.game;

import java.util.Scanner;

/**
 * Created by thibautdebroca on 11/11/2017.
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game(2.0, 3, 3);
        game.addPlayer("Player 1");
        game.addPlayer("Player 2");
        game.launch();
    }

    public void launchGames() {
        while (true) {
            int numPlayers = ask();
            Game game = new Game(2.0, 3, 3);
            game.launch();
            System.out.println("Let's replay");
        }
    }

    public int ask() {
        System.out.println("How many players do you want ?");
        int numPlayers = new Scanner(System.in).nextInt();
        return numPlayers;
    }
 }
