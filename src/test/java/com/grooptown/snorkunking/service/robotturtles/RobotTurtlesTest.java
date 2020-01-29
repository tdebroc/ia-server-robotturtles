package com.grooptown.snorkunking.service.robotturtles;

import com.grooptown.snorkunking.service.engine.game.Game;
import com.grooptown.snorkunking.service.engine.move.AllMove;
import com.grooptown.snorkunking.service.engine.move.CompleteMove;
import com.grooptown.snorkunking.service.engine.move.ExecuteMove;
import com.grooptown.snorkunking.service.engine.player.Player;
import org.junit.Test;

import static com.grooptown.snorkunking.service.engine.card.CardService.*;

public class RobotTurtlesTest {

    @Test
    public void pickCardEvenWhenThereIsNoLeft() {
        Game game = new Game();
        game.addPlayer("Titi");
        game.addPlayer("Toto");
        game.startGame();
        for (int i = 0; i < 7; i ++) {
            playTurnAddAllHandCards(game);
        }
        playTurnExecute(game);
        System.out.println(game);
        playTurnAddAllHandCards(game);
        playTurnExecute(game);
        System.out.println(game);
        playTurnAddAllHandCards(game);
    }

    private void playTurnExecute(Game game) {
        AllMove allMove = new AllMove();
        allMove.setMove(new ExecuteMove());
        allMove.getMove().setGame(game);
        game.playMove(allMove);

        allMove.setMove(new ExecuteMove());
        allMove.getMove().setGame(game);
        game.playMove(allMove);
    }

    private void playTurnAddAllHandCards(Game game) {
        System.out.println("Player turn " + game.getTurnCount());
        AllMove allMove = new AllMove();
        CompleteMove addCardMoves = new CompleteMove();
        addCardMoves.setGame(game);
        addCardMoves.constructMoveFromEntry(cardsToChars(getCurrentPlayer(game).getSecrets().getHandCards()));
        allMove.setMove(addCardMoves);
        game.playMove(allMove);

        allMove.setMove(new ExecuteMove());
        allMove.getMove().setGame(game);
        game.playMove(allMove);
    }

    private Player getCurrentPlayer(Game game) {
        return game.getPlayers().get(game.getCurrentIdPlayerTurn());
    }


}
