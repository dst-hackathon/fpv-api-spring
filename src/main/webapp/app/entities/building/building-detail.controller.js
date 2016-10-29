(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('BuildingDetailController', BuildingDetailController);

    BuildingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Building', 'Plan', 'Floor'];

    function BuildingDetailController($scope, $rootScope, $stateParams, previousState, entity, Building, Plan, Floor) {
        var vm = this;

        vm.building = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('fpvApp:buildingUpdate', function(event, result) {
            vm.building = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
