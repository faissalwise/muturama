(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .factory('AffiniteSearch', AffiniteSearch);

    AffiniteSearch.$inject = ['$resource'];

    function AffiniteSearch($resource) {
        var resourceUrl =  'api/_search/affinites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
