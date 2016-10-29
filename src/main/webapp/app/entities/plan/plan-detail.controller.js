(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('PlanDetailController', PlanDetailController);

    PlanDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Plan', 'Building', 'DeskAssignment'];

    function PlanDetailController($scope, $rootScope, $stateParams, previousState, entity, Plan, Building, DeskAssignment) {
        var vm = this;

        vm.plan = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('fpvApp:planUpdate', function(event, result) {
            vm.plan = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
