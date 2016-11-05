(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('DeskMovementDialogController', DeskMovementDialogController);

    DeskMovementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DeskMovement', 'Employee', 'Desk', 'Plan'];

    function DeskMovementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DeskMovement, Employee, Desk, Plan) {
        var vm = this;

        vm.deskMovement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.desks = Desk.query();
        vm.plans = Plan.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.deskMovement.id !== null) {
                DeskMovement.update(vm.deskMovement, onSaveSuccess, onSaveError);
            } else {
                DeskMovement.save(vm.deskMovement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fpvApp:deskMovementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
