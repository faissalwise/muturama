(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('AgentListDialogController', AgentListDialogController);

    AgentListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AgentList', 'City'];

    function AgentListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AgentList, City) {
        var vm = this;

        vm.agentList = entity;

        if(vm.agentList.id = null) {
            vm.agentList.status = true;
        }
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
            if (vm.agentList.id !== null) {
                AgentList.update(vm.agentList, onSaveSuccess, onSaveError);
            } else {
                AgentList.save(vm.agentList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:agentListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
