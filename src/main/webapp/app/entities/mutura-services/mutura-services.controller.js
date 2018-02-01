(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('MuturaServicesController', MuturaServicesController);

    MuturaServicesController.$inject = ['MuturaServices', 'MuturaServicesSearch'];

    function MuturaServicesController(MuturaServices, MuturaServicesSearch) {

        var vm = this;

        vm.muturaServices = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MuturaServices.query(function(result) {
                vm.muturaServices = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MuturaServicesSearch.query({query: vm.searchQuery}, function(result) {
                vm.muturaServices = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
