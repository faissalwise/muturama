(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .factory('AgentListSearch', AgentListSearch);

    AgentListSearch.$inject = ['$resource'];

    function AgentListSearch($resource) {
        var resourceUrl =  'api/_search/agent-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
