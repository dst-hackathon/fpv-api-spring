(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('DeskAssignmentDialogController', DeskAssignmentDialogController);

    DeskAssignmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DeskAssignment', 'Desk', 'Plan', 'Employee'];

    function DeskAssignmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DeskAssignment, Desk, Plan, Employee) {
        var vm = this;

        vm.deskAssignment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.desks = Desk.query();
        vm.plans = Plan.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.deskAssignment.id !== null) {
                DeskAssignment.update(vm.deskAssignment, onSaveSuccess, onSaveError);
            } else {
                DeskAssignment.save(vm.deskAssignment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fpvApp:deskAssignmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
