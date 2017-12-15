(function() {
    'use strict';
    angular
        .module('muturamaApp')
        .factory('AgentList', AgentList);

    AgentList.$inject = ['$resource'];

    function AgentList ($resource) {
        var resourceUrl =  'api/agent-lists/:id';

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
