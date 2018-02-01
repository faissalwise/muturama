(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('InfohainDeleteController',InfohainDeleteController);

    InfohainDeleteController.$inject = ['$uibModalInstance', 'entity', 'Infohain'];

    function InfohainDeleteController($uibModalInstance, entity, Infohain) {
        var vm = this;

        vm.infohain = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Infohain.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
