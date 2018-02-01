(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('AgentDetailController', AgentDetailController);

    AgentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Agent', 'City'];

    function AgentDetailController($scope, $rootScope, $stateParams, previousState, entity, Agent, City) {
        var vm = this;

        vm.agent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('muturamaApp:agentUpdate', function(event, result) {
            vm.agent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
