(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('PlanDialogController', PlanDialogController);

    PlanDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Plan', 'Floor', 'DeskAssignment'];

    function PlanDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Plan, Floor, DeskAssignment) {
        var vm = this;

        vm.plan = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.floors = Floor.query();
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


        vm.setImage = function ($file, plan) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        plan.image = base64Data;
                        plan.imageContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.effectiveDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
