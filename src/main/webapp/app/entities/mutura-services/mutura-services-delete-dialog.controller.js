(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('MuturaServicesDeleteController',MuturaServicesDeleteController);

    MuturaServicesDeleteController.$inject = ['$uibModalInstance', 'entity', 'MuturaServices'];

    function MuturaServicesDeleteController($uibModalInstance, entity, MuturaServices) {
        var vm = this;

        vm.muturaServices = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MuturaServices.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
