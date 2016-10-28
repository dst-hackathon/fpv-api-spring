(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('DeskDetailController', DeskDetailController);

    DeskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Desk', 'Floor'];

    function DeskDetailController($scope, $rootScope, $stateParams, previousState, entity, Desk, Floor) {
        var vm = this;

        vm.desk = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('fpvApp:deskUpdate', function(event, result) {
            vm.desk = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
