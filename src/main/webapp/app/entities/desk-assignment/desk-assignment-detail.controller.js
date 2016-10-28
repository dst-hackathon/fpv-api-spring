(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('DeskAssignmentDetailController', DeskAssignmentDetailController);

    DeskAssignmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DeskAssignment', 'Desk', 'Plan', 'Employee'];

    function DeskAssignmentDetailController($scope, $rootScope, $stateParams, previousState, entity, DeskAssignment, Desk, Plan, Employee) {
        var vm = this;

        vm.deskAssignment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('fpvApp:deskAssignmentUpdate', function(event, result) {
            vm.deskAssignment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
