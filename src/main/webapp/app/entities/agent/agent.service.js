(function() {
    'use strict';
    angular
        .module('muturamaApp')
        .factory('Agent', Agent);

    Agent.$inject = ['$resource'];

    function Agent ($resource) {
        var resourceUrl =  'api/agents/:id';

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
