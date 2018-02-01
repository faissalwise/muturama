(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .factory('MuturaServicesSearch', MuturaServicesSearch);

    MuturaServicesSearch.$inject = ['$resource'];

    function MuturaServicesSearch($resource) {
        var resourceUrl =  'api/_search/mutura-services/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
