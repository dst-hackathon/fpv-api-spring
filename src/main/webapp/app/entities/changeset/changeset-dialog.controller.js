(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('ChangesetDialogController', ChangesetDialogController);

    ChangesetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Changeset', 'Plan', 'ChangesetItem'];

    function ChangesetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Changeset, Plan, ChangesetItem) {
        var vm = this;

        vm.changeset = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.plans = Plan.query();
        vm.changesetitems = ChangesetItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.changeset.id !== null) {
                Changeset.update(vm.changeset, onSaveSuccess, onSaveError);
            } else {
                Changeset.save(vm.changeset, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fpvApp:changesetUpdate', result);
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
