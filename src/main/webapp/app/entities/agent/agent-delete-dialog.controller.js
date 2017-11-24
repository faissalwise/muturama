(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('AgentDeleteController',AgentDeleteController);

    AgentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Agent'];

    function AgentDeleteController($uibModalInstance, entity, Agent) {
        var vm = this;

        vm.agent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Agent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
