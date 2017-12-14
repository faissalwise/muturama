(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('AffiniteDetailController', AffiniteDetailController);

    AffiniteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Affinite'];

    function AffiniteDetailController($scope, $rootScope, $stateParams, previousState, entity, Affinite) {
        var vm = this;

        vm.affinite = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('muturamaApp:affiniteUpdate', function(event, result) {
            vm.affinite = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
