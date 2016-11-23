(function() {
    'use strict';
    angular
        .module('fpvApp')
        .factory('ChangesetItem', ChangesetItem);

    ChangesetItem.$inject = ['$resource'];

    function ChangesetItem ($resource) {
        var resourceUrl =  'api/changeset-items/:id';

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
