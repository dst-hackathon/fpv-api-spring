(function() {
    'use strict';
    angular
        .module('fpvApp')
        .factory('Desk', Desk);

    Desk.$inject = ['$resource'];

    function Desk ($resource) {
        var resourceUrl =  'api/desks/:id';

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
