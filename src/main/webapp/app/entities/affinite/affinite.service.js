(function() {
    'use strict';
    angular
        .module('muturamaApp')
        .factory('Affinite', Affinite);

    Affinite.$inject = ['$resource'];

    function Affinite ($resource) {
        var resourceUrl =  'api/affinites/:id';

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
