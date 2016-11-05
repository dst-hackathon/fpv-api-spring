(function() {
    'use strict';
    angular
        .module('fpvApp')
        .factory('Plan', Plan);

    Plan.$inject = ['$resource', 'DateUtils'];

    function Plan ($resource, DateUtils) {
        var resourceUrl =  'api/plans/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.effectiveDate = DateUtils.convertLocalDateFromServer(data.effectiveDate);
                        data.approveDate = DateUtils.convertLocalDateFromServer(data.approveDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.effectiveDate = DateUtils.convertLocalDateToServer(copy.effectiveDate);
                    copy.approveDate = DateUtils.convertLocalDateToServer(copy.approveDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.effectiveDate = DateUtils.convertLocalDateToServer(copy.effectiveDate);
                    copy.approveDate = DateUtils.convertLocalDateToServer(copy.approveDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
