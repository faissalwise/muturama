(function() {
    'use strict';
    angular
        .module('muturamaApp')
        .factory('MuturaServices', MuturaServices);

    MuturaServices.$inject = ['$resource'];

    function MuturaServices ($resource) {
        var resourceUrl =  'api/mutura-services/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
