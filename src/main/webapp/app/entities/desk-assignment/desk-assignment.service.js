(function() {
    'use strict';
    angular
        .module('fpvApp')
        .factory('DeskAssignment', DeskAssignment);

    DeskAssignment.$inject = ['$resource'];

    function DeskAssignment ($resource) {
        var resourceUrl =  'api/desk-assignments/:id';

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
