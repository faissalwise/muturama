(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('InfohainDialogController', InfohainDialogController);

    InfohainDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Infohain'];

    function InfohainDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Infohain) {
        var vm = this;

        vm.infohain = entity;
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
            if (vm.infohain.id !== null) {
                Infohain.update(vm.infohain, onSaveSuccess, onSaveError);
            } else {
                Infohain.save(vm.infohain, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:infohainUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
