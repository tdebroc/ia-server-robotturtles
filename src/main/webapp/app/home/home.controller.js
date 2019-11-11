(function() {
    'use strict';

    angular
        .module('iaserversnorkunkingApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state',
                              'IAConnectorGame', 'IAConnectorService', 'IAConnectorSocketService'];

    function HomeController ($scope, Principal, LoginService, $state,
                            IAConnectorGame, IAConnectorService, IAConnectorSocketService) {

        $scope.moves = ["Go Up", "Go Down", "Pick Treasure"];
        /*$scope.oxygenFactor = 2.0
        $scope.caveCount = 3
        $scope.caveWidth = 1;*/


        $scope.currentPlayers = {};
        function loadCurrentPlayers() {
            var currentPlayerString = localStorage.getItem("currentPlayers");
            if (!currentPlayerString) {
                $scope.currentPlayers = {};
            } else {
                $scope.currentPlayers = JSON.parse(currentPlayerString);
            }
        }
        loadCurrentPlayers();

        console.log("$scope.currentPlayers", $scope.currentPlayers);

        //=====================================================================
        //= Reboot
        //=====================================================================
        $scope.reboot = function() {
            IAConnectorService.init();
            localStorage.clear();
            loadCurrentPlayers();
            $scope.games = undefined;
            $scope.refreshGameList();
        }
        //=====================================================================
        // Sockets.
        //=====================================================================
        IAConnectorSocketService.connect();
        IAConnectorSocketService.receive().then(null, null, function(game) {
            console.log("from socket", game);
            $scope.currentGame = game;
            $scope.currentIdGame = game.idGame;
            console.log()
        });
        IAConnectorSocketService.subscribe();
        IAConnectorSocketService.subscribeRefreshAllGames();

        IAConnectorSocketService.receiveAllGames().then(null, null, function(allGames) {
            $scope.games = allGames;
            console.log("from socket allGames", allGames);
        })


        var vm = this;
        $scope.currentGame;
        $scope.currentIdGame;


        $scope.createGame = function() {

            IAConnectorService.createGame()
                .then(function(res) {
                    var game = res.data;
                    console.log("Game is : " + JSON.stringify(game));
                    $scope.currentGame = game;
                    $scope.refreshGameList();
                    $scope.currentIdGame = game.idGame;
                })

        }

        $scope.selectGame = function(idGame) {
            console.log(idGame);
            IAConnectorGame
                .get({idGame : idGame})
                .$promise.then(function(currentGame) {
                    console.log(currentGame);
                    $scope.currentIdGame = idGame;
                    $scope.currentGame = currentGame;
                 });;
        }





        /**
         * Refresh List of Games.
         */
        $scope.refreshGameList = function(callback) {
            console.log("get game list");
            IAConnectorService.getGames(function(res) {
                var games = res.data
                if (!$scope.games) {
                    $scope.selectGame(games[0]);
                }
                $scope.games = games;
                console.log($scope.games);
            })
        }
        $scope.refreshGameList();

        function refreshCurrentGame() {
            $scope.selectGame($scope.currentIdGame);
        }

        $scope.startGame = function() {
            var idGame = $scope.currentIdGame;
            IAConnectorService
                    .startGame(idGame)
                    .then(refreshCurrentGame);
        }

        $scope.playerName =  chance.name({ nationality: "it" })

        $scope.shouldSeeInfo = function(player) {
            for (var key in $scope.currentPlayers) {
                var idPlayerTurn = getPlayerTurnIdFromKey(key);
                var idGame = getIdGameFromKey(key);
                if ($scope.currentIdGame == idGame
                    && idPlayerTurn == player.playerNumber - 1) {
                    return true;
                }
            }
            return false;
        }

        function getPlayerTurnIdFromKey(key) {
            return key.split("#")[1];
        }

        function getIdGameFromKey(key) {
            return key.split("#")[0];
        }

        $scope.addPlayer= function() {
            var idGame = $scope.currentIdGame;
            var playerName = $("#playerName").val()
            $scope.playerName =  chance.name({ nationality: "it" })

            IAConnectorService.addPlayer(idGame, playerName, function(response) {
                var playerInstance = response.data
                var idPlayerTurn = playerInstance.idPlayer;
                var key = getKey(idGame, idPlayerTurn);
                $scope.currentPlayers[key] = playerInstance.UUID || playerInstance.uuid;
                localStorage.setItem("currentPlayers", JSON.stringify($scope.currentPlayers));

                refreshCurrentGame();
            })
        }

        $scope.sendMove = function(idPlayerTurn) {
            $scope.moveEntry = $("#moveEntry").val();
            var playerUUID = $scope.currentPlayers[getKey($scope.currentIdGame, idPlayerTurn)];
            IAConnectorService.sendMove(playerUUID, $scope.moveEntry).then(
                function(response) {
                    refreshCurrentGame();
                }, function(response) {
                    console.log("response", response)
                    alert(JSON.stringify(response.data));
                }
            )
        }

        function getKey(idGame, idPlayerTurn) {
            return idGame + "#" + idPlayerTurn;
        }

        $scope.isCurrentPlayer = function(currentIdGame, idPlayerTurn) {
            console.log(currentIdGame, idPlayerTurn, getKey(currentIdGame, idPlayerTurn));
            return $scope.currentPlayers[getKey(currentIdGame, idPlayerTurn)]
        }

        $scope.isPlayerTurn = function(game, idPlayerTurn) {
            return game.started && game.currentIdPlayerTurn == idPlayerTurn && !game.finished;
        }


        $scope.getPlayersInCell = function(cellIndex, levelIndex, caveIndex) {
            var playersInCell = []
            for (var i = 0; i < $scope.currentGame.players.length; i++) {
                var player = $scope.currentGame.players[i]
                if (player.caveIndex == caveIndex && player.levelIndex == levelIndex &&
                        player.cellIndex == cellIndex) {
                    player.index = i;
                    playersInCell.push(player)
                }
            }
            return playersInCell;
        }

        $scope.getPlayersAtSurface = function() {
            var playersAtSurface = []
            if (!$scope.currentGame) return []
            for (var i = 0; i < $scope.currentGame.players.length; i++) {
                var player = $scope.currentGame.players[i];
                if (player.caveIndex == null && player.levelIndex == null) {
                    player.index = i;
                    playersAtSurface.push(player)
                }
            }
            return playersAtSurface;
        }

    }


})();
