(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('AgentListDetailController', AgentListDetailController);

    AgentListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AgentList', 'City'];

    function AgentListDetailController($scope, $rootScope, $stateParams, previousState, entity, AgentList, City) {
        var vm = this;

        vm.agentList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('muturamaApp:agentListUpdate', function(event, result) {
            vm.agentList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
