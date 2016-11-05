(function() {
    'use strict';
    angular
        .module('fpvApp')
        .factory('Shadow', Shadow);

    Shadow.$inject = ['$resource'];

    function Shadow ($resource) {
        var resourceUrl =  'api/shadows/:id';

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
