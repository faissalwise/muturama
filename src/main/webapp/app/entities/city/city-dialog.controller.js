(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('CityDialogController', CityDialogController);

    CityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'City', 'Agent'];

    function CityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, City, Agent) {
        var vm = this;

        vm.city = entity;
        vm.clear = clear;
        vm.save = save;
        vm.agents = Agent.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.city.id !== null) {
                City.update(vm.city, onSaveSuccess, onSaveError);
            } else {
                City.save(vm.city, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:cityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
