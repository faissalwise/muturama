(function() {
    'use strict';
    angular
        .module('muturamaApp')
        .factory('Infohain', Infohain);

    Infohain.$inject = ['$resource'];

    function Infohain ($resource) {
        var resourceUrl =  'api/infohains/:id';

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
