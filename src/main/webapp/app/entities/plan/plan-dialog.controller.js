(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('PlanDialogController', PlanDialogController);

    PlanDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Plan', 'Building', 'DeskAssignment'];

    function PlanDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Plan, Building, DeskAssignment) {
        var vm = this;

        vm.plan = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.buildings = Building.query();
        vm.deskassignments = DeskAssignment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.plan.id !== null) {
                Plan.update(vm.plan, onSaveSuccess, onSaveError);
            } else {
                Plan.save(vm.plan, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fpvApp:planUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.effectiveDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
