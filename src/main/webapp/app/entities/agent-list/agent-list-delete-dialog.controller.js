(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('AgentListDeleteController',AgentListDeleteController);

    AgentListDeleteController.$inject = ['$uibModalInstance', 'entity', 'AgentList'];

    function AgentListDeleteController($uibModalInstance, entity, AgentList) {
        var vm = this;

        vm.agentList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AgentList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
