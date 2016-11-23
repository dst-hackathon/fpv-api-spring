(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('ChangesetItemDetailController', ChangesetItemDetailController);

    ChangesetItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChangesetItem', 'Employee', 'Desk', 'Changeset'];

    function ChangesetItemDetailController($scope, $rootScope, $stateParams, previousState, entity, ChangesetItem, Employee, Desk, Changeset) {
        var vm = this;

        vm.changesetItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('fpvApp:changesetItemUpdate', function(event, result) {
            vm.changesetItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
