(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('PlanDetailController', PlanDetailController);

    PlanDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Plan', 'Floor', 'DeskAssignment'];

    function PlanDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Plan, Floor, DeskAssignment) {
        var vm = this;

        vm.plan = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('fpvApp:planUpdate', function(event, result) {
            vm.plan = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
