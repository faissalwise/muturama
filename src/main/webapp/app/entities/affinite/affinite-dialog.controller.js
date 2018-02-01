(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('AffiniteDialogController', AffiniteDialogController);

    AffiniteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Affinite'];

    function AffiniteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Affinite) {
        var vm = this;

        vm.affinite = entity;
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
            if (vm.affinite.id !== null) {
                Affinite.update(vm.affinite, onSaveSuccess, onSaveError);
            } else {
                Affinite.save(vm.affinite, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:affiniteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
