(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('DeskMovementDetailController', DeskMovementDetailController);

    DeskMovementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DeskMovement', 'Employee', 'Desk', 'Plan'];

    function DeskMovementDetailController($scope, $rootScope, $stateParams, previousState, entity, DeskMovement, Employee, Desk, Plan) {
        var vm = this;

        vm.deskMovement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('fpvApp:deskMovementUpdate', function(event, result) {
            vm.deskMovement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
