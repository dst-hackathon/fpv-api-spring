(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('FloorDetailController', FloorDetailController);

    FloorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Floor', 'Plan', 'Desk'];

    function FloorDetailController($scope, $rootScope, $stateParams, previousState, entity, Floor, Plan, Desk) {
        var vm = this;

        vm.floor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('fpvApp:floorUpdate', function(event, result) {
            vm.floor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
