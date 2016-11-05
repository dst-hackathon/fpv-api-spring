(function() {
    'use strict';
    angular
        .module('fpvApp')
        .factory('DeskMovement', DeskMovement);

    DeskMovement.$inject = ['$resource'];

    function DeskMovement ($resource) {
        var resourceUrl =  'api/desk-movements/:id';

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
