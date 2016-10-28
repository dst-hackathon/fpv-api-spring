(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('FloorDialogController', FloorDialogController);

    FloorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Floor', 'Plan', 'Desk'];

    function FloorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Floor, Plan, Desk) {
        var vm = this;

        vm.floor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.plans = Plan.query();
        vm.desks = Desk.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.floor.id !== null) {
                Floor.update(vm.floor, onSaveSuccess, onSaveError);
            } else {
                Floor.save(vm.floor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fpvApp:floorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
