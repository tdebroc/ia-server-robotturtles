(function() {
    'use strict';

    angular
        .module('iaserversnorkunkingApp')
        .factory('IAConnectorGame', IAConnectorGame);

    IAConnectorGame.$inject = ['$resource'];

    function IAConnectorGame ($resource) {
        var service = $resource('api/iaconnector/game/:idGame', {idGame:'@idGame'}, {
            'get': { method: 'GET', params: {}, isArray: false}
        });

        return service;
    }
})();
