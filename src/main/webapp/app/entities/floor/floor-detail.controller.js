(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('FloorDetailController', FloorDetailController);

    FloorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Floor', 'Building', 'Desk'];

    function FloorDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Floor, Building, Desk) {
        var vm = this;

        vm.floor = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('fpvApp:floorUpdate', function(event, result) {
            vm.floor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
