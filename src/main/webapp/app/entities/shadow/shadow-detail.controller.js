(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('ShadowDetailController', ShadowDetailController);

    ShadowDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Shadow', 'Employee', 'Desk'];

    function ShadowDetailController($scope, $rootScope, $stateParams, previousState, entity, Shadow, Employee, Desk) {
        var vm = this;

        vm.shadow = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('fpvApp:shadowUpdate', function(event, result) {
            vm.shadow = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
