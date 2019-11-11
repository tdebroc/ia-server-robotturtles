(function () {
    'use strict';

    angular
        .module('iaserversnorkunkingApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
