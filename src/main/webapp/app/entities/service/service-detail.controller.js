(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('ServiceDetailController', ServiceDetailController);

    ServiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Service', 'User'];

    function ServiceDetailController($scope, $rootScope, $stateParams, previousState, entity, Service, User) {
        var vm = this;

        vm.service = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('muturamaApp:serviceUpdate', function(event, result) {
            vm.service = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
