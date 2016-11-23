(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('ChangesetItemDialogController', ChangesetItemDialogController);

    ChangesetItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChangesetItem', 'Employee', 'Desk', 'Changeset'];

    function ChangesetItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ChangesetItem, Employee, Desk, Changeset) {
        var vm = this;

        vm.changesetItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.desks = Desk.query();
        vm.changesets = Changeset.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.changesetItem.id !== null) {
                ChangesetItem.update(vm.changesetItem, onSaveSuccess, onSaveError);
            } else {
                ChangesetItem.save(vm.changesetItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fpvApp:changesetItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
