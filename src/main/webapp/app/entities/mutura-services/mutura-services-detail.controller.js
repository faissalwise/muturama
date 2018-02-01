(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('MuturaServicesDetailController', MuturaServicesDetailController);

    MuturaServicesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MuturaServices'];

    function MuturaServicesDetailController($scope, $rootScope, $stateParams, previousState, entity, MuturaServices) {
        var vm = this;

        vm.muturaServices = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('muturamaApp:muturaServicesUpdate', function(event, result) {
            vm.muturaServices = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
