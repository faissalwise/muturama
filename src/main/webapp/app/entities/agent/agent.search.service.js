(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .factory('AgentSearch', AgentSearch);

    AgentSearch.$inject = ['$resource'];

    function AgentSearch($resource) {
        var resourceUrl =  'api/_search/agents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
