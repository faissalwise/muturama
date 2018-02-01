(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('InfohainDetailController', InfohainDetailController);

    InfohainDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Infohain'];

    function InfohainDetailController($scope, $rootScope, $stateParams, previousState, entity, Infohain) {
        var vm = this;

        vm.infohain = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('muturamaApp:infohainUpdate', function(event, result) {
            vm.infohain = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
