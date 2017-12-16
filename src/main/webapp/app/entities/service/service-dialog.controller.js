(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('ServiceDialogController', ServiceDialogController);

    ServiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Service', 'User'];

    function ServiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Service, User) {
        var vm = this;

        vm.service = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.service.id !== null) {
                Service.update(vm.service, onSaveSuccess, onSaveError);
            } else {
                Service.save(vm.service, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:serviceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
