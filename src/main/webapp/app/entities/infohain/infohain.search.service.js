(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .factory('InfohainSearch', InfohainSearch);

    InfohainSearch.$inject = ['$resource'];

    function InfohainSearch($resource) {
        var resourceUrl =  'api/_search/infohains/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
