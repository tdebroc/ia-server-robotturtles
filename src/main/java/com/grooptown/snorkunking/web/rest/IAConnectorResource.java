package com.grooptown.snorkunking.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grooptown.snorkunking.service.engine.game.Game;
import com.grooptown.snorkunking.service.engine.move.AllMove;
import com.grooptown.snorkunking.service.engine.player.Player;
import com.grooptown.snorkunking.service.engine.connector.PlayerInstance;
import com.grooptown.snorkunking.service.engine.connector.MessageResponse;
import com.grooptown.snorkunking.service.engine.player.PlayerSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by thibautdebroca on 26/11/2017.
 */
@RestController
@RequestMapping("/api/iaconnector")
public class IAConnectorResource {

    @Autowired
    private
    SimpMessageSendingOperations messagingTemplate;

    private static Map<Integer, Game> gamesMap = new HashMap<>();

    private static int NEXT_GAME_ID = getNextGameIdFromFile();

    private static Map<String, PlayerInstance> playersInstances = new HashMap<>();

    public IAConnectorResource() {
        init();
    }

    @GetMapping("/init")
    public void init() {
        NEXT_GAME_ID = getNextGameIdFromFile();
        gamesMap = new HashMap<>();
        createNewGame();
    }

    @GetMapping("/game")
    public Game createNewGame() {
        int idGame = addGameToGamesMap();
        refreshGames();
        return gamesMap.get(idGame);
    }

    @GetMapping("/game/{idGame}")
    public Game getGame(@PathVariable Integer idGame) throws IOException {
        System.out.println("Get Game: " + idGame);
        return filterGame(gamesMap.get(idGame));
    }

    @GetMapping("/player/secrets/{uuid}")
    public ResponseEntity<PlayerSecret> getPlayerSecrets(@PathVariable String uuid) throws Exception {
        PlayerInstance playerInstance = playersInstances.get(uuid);
        if (playerInstance == null) {
            throw new Exception("UUID: " + uuid + " not known.");
        }
        Game game = gamesMap.get(playerInstance.getIdGame());
        Player playerFromInstance = playerInstance.getPlayerFromInstance(game);
        return new ResponseEntity<>(playerFromInstance.getSecrets(), HttpStatus.OK);

    }


    @GetMapping("/games")
    public Set<Integer> getGames() {
        return gamesMap.keySet();
    }

    @GetMapping(value = "/addPlayer")
    public PlayerInstance addPlayer(@RequestParam(value = "idGame") int idGame,
                                    @RequestParam(value = "playerName", required = false) String playerName) throws IOException {
        Game game = gamesMap.get(idGame);
        if (game.isStarted() ||
            game.getPlayers().size() >= Game.MAX_NUM_PLAYER) {
            return null;
        }
        String userId;
        do {
            userId = UUID.randomUUID().toString();
        } while (playersInstances.containsKey(userId));

        PlayerInstance playerInstance = new PlayerInstance(idGame, game.getPlayers().size(), userId);
        game.addPlayer(playerName);
        refreshGame(game);
        playersInstances.put(userId, playerInstance);
        return playerInstance;
    }

    @GetMapping(value = "/startGame")
    public boolean startGame(@RequestParam(value = "idGame") int idGame) throws IOException {
        Game game = gamesMap.get(idGame);
        game.startGame();
        refreshGame(game);
        return true;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/sendMove")
    public ResponseEntity<MessageResponse> sendMove(@RequestParam(value = "playerUUID") String playerUUID,
                                                    @RequestParam(value = "move") String moveString) throws IOException {
        System.out.println(playerUUID);
        PlayerInstance playerInstance = playersInstances.get(playerUUID);
        System.out.println(playerInstance);
        System.out.println(playersInstances);
        if (playerInstance == null) {
            return sendBadRequest("Unknown User");
        }
        Game game = gamesMap.get(playerInstance.getIdGame());
        if (game == null) {
            return sendBadRequest("Unknown Game");
        }
        if (!game.isStarted()) {
            return sendBadRequest("Game has not started.");
        }
        if (game.isFinished()) {
            return sendBadRequest("Game is Finished.");
        }
        if (!playersInstances.containsKey(playerUUID)) {
            return sendBadRequest("Unknown player");
        }
        if (game.getCurrentIdPlayerTurn() != playerInstance.getIdPlayer()) {
            return sendBadRequest("It's not the turn of player " + (playerInstance.getIdPlayer() + 1));
        }
        AllMove move = game.getMoveFromString(moveString);

        if (move == null) {
            return sendBadRequest("Wrong Move");
        }

        game.playMove(move);
        // RecordMove recordMove = new RecordMove(moveString, playerInstance.getIdPlayer());
        // game.getMoveList().add(recordMove);
        // game.getCurrentStage().prepareMove(game);
        refreshGame(game);
        if (game.isFinished()) {
            writeToFile("game" + game.getIdGame(), game.asJson());
        }
        return sendValidResponse("OK");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getOpponentMoves")
    public ResponseEntity<Game> getOpponentMoves(@RequestParam(value = "playerUUID") String playerUUID) throws URISyntaxException, InterruptedException {
        PlayerInstance playerInstance = playersInstances.get(playerUUID);
        if (playerInstance == null) {
            return null;
        }
        Game game = gamesMap.get(playerInstance.getIdGame());
        int timeRequest = 0;
        try {
            while (game.getCurrentIdPlayerTurn() != playerInstance.getIdPlayer() || !game.isStarted()) {
                int sleepDuration = 1000;
                // System.out.println("Sleeping for " + sleepDuration + "ms");
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // restore interrupted status
                }
                timeRequest += sleepDuration;
                if (timeRequest > 60 * 1000 * 10) {
                    return null;
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Run time catched for " + e + "");
        }
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    private int addGameToGamesMap() {
        int newLyGameId = NEXT_GAME_ID;
        Game game = new Game(newLyGameId);
        gamesMap.put(newLyGameId, game);
        NEXT_GAME_ID++;
        saveNextGameId(NEXT_GAME_ID);
        return newLyGameId;
    }

    public ResponseEntity<MessageResponse> sendBadRequest(String message) {
        return new ResponseEntity<>(new MessageResponse(message, null), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<MessageResponse> sendValidResponse(String message) {
        return new ResponseEntity<>(new MessageResponse(null, message), HttpStatus.OK);
    }

    //==========================================================================================
    //= Filter Information for front
    //==========================================================================================

    private Game filterGame(Game game) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.enableDefaultTyping();
        Game gameDeepCopy = objectMapper
            .readValue(objectMapper.writeValueAsString(game), Game.class);
        for (Player player : gameDeepCopy.getPlayers()) {
            player.clearSecrets();
        }
        return game;
    }


    //==========================================================================================
    //= Persit
    //==========================================================================================

    private static String lastGameIdFile = "lastGameId.txt";

    private static Integer getNextGameIdFromFile() {
        try {
            String collect = Files.lines(Paths.get(lastGameIdFile)).collect(Collectors.toList())
                .get(0);
            return Integer.parseInt(collect);
        } catch (Exception e) {
            return 1;
        }
    }

    private void saveNextGameId(int nextGameId) {
        writeToFile(lastGameIdFile, "" + nextGameId);
    }

    private void writeToFile(String fileName, String content) {
        try {
            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //==================================================================================================================
    //= Sockets
    //==================================================================================================================
    private void refreshGames() {
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/refreshGames", getGames());
        }
    }

    private void refreshGame(Game game) throws IOException {
        messagingTemplate.convertAndSend("/topic/refreshGame", filterGame(game));
    }




}
