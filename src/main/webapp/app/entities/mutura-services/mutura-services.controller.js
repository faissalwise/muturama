(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('MuturaServicesController', MuturaServicesController);

    MuturaServicesController.$inject = ['MuturaServices'];

    function MuturaServicesController(MuturaServices) {

        var vm = this;

        vm.muturaServices = [];

        loadAll();

        function loadAll() {
            MuturaServices.query(function(result) {
                vm.muturaServices = result;
                vm.searchQuery = null;
            });
        }
    }
})();
