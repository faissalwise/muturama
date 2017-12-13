(function () {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('HomeController', HomeController)



        // formats a number as a latitude (e.g. 40.46... => "40°27'44"N")
        .filter('lat', function () {
            return function (input, decimals) {
                if (!decimals) decimals = 0;
                input = input * 1;
                var ns = input > 0 ? "N" : "S";
                input = Math.abs(input);
                var deg = Math.floor(input);
                var min = Math.floor((input - deg) * 60);
                var sec = ((input - deg - min / 60) * 3600).toFixed(decimals);
                return deg + "°" + min + "'" + sec + '"' + ns;
            }
        })

        // formats a number as a longitude (e.g. -80.02... => "80°1'24"W")
        .filter('lon', function () {
            return function (input, decimals) {
                if (!decimals) decimals = 0;
                input = input * 1;
                var ew = input > 0 ? "E" : "W";
                input = Math.abs(input);
                var deg = Math.floor(input);
                var min = Math.floor((input - deg) * 60);
                var sec = ((input - deg - min / 60) * 3600).toFixed(decimals);
                return deg + "°" + min + "'" + sec + '"' + ew;
            }
        })

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


        // current location to set maps first location on load
        $scope.loc = {lat: 31, lon: -7};
        $scope.gotoCurrentLocation = function () {
            if ("geolocation" in navigator) {
                navigator.geolocation.getCurrentPosition(function (position) {
                    var c = position.coords;
                    $scope.gotoLocation(c.latitude, c.longitude);
                });
                return true;
            }
            return false;
        };
        $scope.gotoLocation = function (lat, lon) {
            if ($scope.lat != lat || $scope.lon != lon) {
                $scope.loc = {lat: lat, lon: lon};
                if (!$scope.$$phase) $scope.$apply("loc");
            }
        };

        // geo-coding
        $scope.search = "";
        $scope.geoCode = function () {
            if ($scope.search && $scope.search.length > 0) {
                if (!this.geocoder) this.geocoder = new google.maps.Geocoder();
                this.geocoder.geocode({'address': $scope.search}, function (results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        var loc = results[0].geometry.location;
                        $scope.search = results[0].formatted_address;
                        $scope.gotoLocation(loc.lat(), loc.lng());
                    } else {
                        alert("Sorry, this search produced no results.");
                    }
                });
            }
        };


        var agentList = AgentList.query({size: 500000}, function (result, headers) {
            return result;
        });
        $scope.getAgentsListBycity = function (select) {
            $scope.agentLists = [];
            if (select != null) {
                angular.forEach(agentList, function (results) {
                    //console.log(dlContCatSet.dlContTypeSet);
                    if (select.id == results.city.id && results.status == true) {
                        $scope.agentLists.push(results);
                    }
                });
            } else {
                angular.forEach(agentList, function (results) {
                    //console.log(dlContCatSet.dlContTypeSet);
                    if (results.status == true) {
                        $scope.agentLists.push(results);
                    }
                });
            }

        };

    }
})();
