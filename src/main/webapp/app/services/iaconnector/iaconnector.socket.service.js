(function() {
    'use strict';
    /* globals SockJS, Stomp */

    angular
        .module('iaserversnorkunkingApp')
        .factory('IAConnectorSocketService', IAConnectorSocketService);

    IAConnectorSocketService.$inject = ['$rootScope', '$window', '$cookies', '$http', '$q'];

    function IAConnectorSocketService ($rootScope, $window, $cookies, $http, $q) {
        var stompClient = null;
        var subscriber = null;
        var subscriberAllGames = null;

        var listener = $q.defer();
        var listenerAllGames = $q.defer();

        var connected = $q.defer();
        var alreadyConnectedOnce = false;

        var service = {
            connect: connect,
            disconnect: disconnect,
            receive: receive,
            receiveAllGames: receiveAllGames,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
            subscribeRefreshAllGames : subscribeRefreshAllGames
        };

        return service;

        function connect () {

            // building absolute path so that websocket doesnt fail when deploying with a context path
            var loc = $window.location;
            var url = '//' + loc.host + loc.pathname + 'websocket/snorkunking';
            var socket = new SockJS(url);
            stompClient = Stomp.over(socket);
            stompClient.debug = null
            var stateChangeStart;
            var headers = {};
            headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
            stompClient.connect(headers, function() {
                connected.resolve('success');
                // sendActivity();
                if (!alreadyConnectedOnce) {
                    stateChangeStart = $rootScope.$on('$stateChangeStart', function () {
                        // sendActivity();
                    });
                    alreadyConnectedOnce = true;
                }
            });
            $rootScope.$on('$destroy', function () {
                if(angular.isDefined(stateChangeStart) && stateChangeStart !== null){
                    stateChangeStart();
                }
            });
        }

        function disconnect () {
            if (stompClient !== null) {
                stompClient.disconnect();
                stompClient = null;
            }
        }

        function receive () {
            return listener.promise;
        }

        /*function sendActivity() {
            if (stompClient !== null && stompClient.connected) {
                stompClient
                    .send('/topic/activity',
                    {},
                    angular.toJson({'page': $rootScope.toState.name}));
            }
        }*/

        function subscribe () {
            connected.promise.then(function() {
                subscriber = stompClient.subscribe('/topic/refreshGame', function(data) {
                    listener.notify(angular.fromJson(data.body));
                });
            }, null, null);
        }

        function subscribeRefreshAllGames() {
            connected.promise.then(function() {
                subscriberAllGames = stompClient.subscribe('/topic/refreshGames', function(data) {
                    listenerAllGames.notify(angular.fromJson(data.body));
                });
            }, null, null);
        }

        function receiveAllGames() {
            return listenerAllGames.promise;
        }

        function unsubscribe () {
            if (subscriber !== null) {
                subscriber.unsubscribe();
            }
            listener = $q.defer();
        }
    }
})();
