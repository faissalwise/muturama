(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('AgentDialogController', AgentDialogController);

    AgentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Agent', 'City'];

    function AgentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Agent, City) {
        var vm = this;

        vm.agent = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cities = City.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.agent.id !== null) {
                Agent.update(vm.agent, onSaveSuccess, onSaveError);
            } else {
                Agent.save(vm.agent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:agentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
