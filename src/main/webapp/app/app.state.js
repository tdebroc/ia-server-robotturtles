(function() {
    'use strict';

    angular
        .module('iaserversnorkunkingApp')
        .filter('newlines', function () {
           return function(text) {
             return text.replace(/(&#13;)?&#10;/g, '<br/>');
           }
         })
        .filter('range', function() {
           return function(input, total) {
             total = parseInt(total);

             for (var i=0; i<total; i++) {
               input.push(i);
             }

             return input;
           };
        })
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        });
    }
})();
