(function() {
    'use strict';
    angular
        .module('fpvApp')
        .factory('Changeset', Changeset);

    Changeset.$inject = ['$resource', 'DateUtils'];

    function Changeset ($resource, DateUtils) {
        var resourceUrl =  'api/changesets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.effectiveDate = DateUtils.convertLocalDateFromServer(data.effectiveDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.effectiveDate = DateUtils.convertLocalDateToServer(copy.effectiveDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.effectiveDate = DateUtils.convertLocalDateToServer(copy.effectiveDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
