(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('AffiniteDeleteController',AffiniteDeleteController);

    AffiniteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Affinite'];

    function AffiniteDeleteController($uibModalInstance, entity, Affinite) {
        var vm = this;

        vm.affinite = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Affinite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
