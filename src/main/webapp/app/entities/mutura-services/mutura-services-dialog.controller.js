(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('MuturaServicesDialogController', MuturaServicesDialogController);

    MuturaServicesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MuturaServices'];

    function MuturaServicesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MuturaServices) {
        var vm = this;

        vm.muturaServices = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.muturaServices.id !== null) {
                MuturaServices.update(vm.muturaServices, onSaveSuccess, onSaveError);
            } else {
                MuturaServices.save(vm.muturaServices, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:muturaServicesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
