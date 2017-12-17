(function () {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('HomeController', HomeController)



        
    // - Documentation: https://developers.google.com/maps/documentation/


    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'AgentList', 'City'];

    function HomeController($scope, Principal, LoginService, $state, AgentList, City) {
        var vm = this;
        vm.cities = City.query();
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register() {
            $state.go('register');
        }


 

    }
})();
