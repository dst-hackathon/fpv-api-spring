(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('ChangesetDetailController', ChangesetDetailController);

    ChangesetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Changeset', 'Plan', 'ChangesetItem'];

    function ChangesetDetailController($scope, $rootScope, $stateParams, previousState, entity, Changeset, Plan, ChangesetItem) {
        var vm = this;

        vm.changeset = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('fpvApp:changesetUpdate', function(event, result) {
            vm.changeset = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
