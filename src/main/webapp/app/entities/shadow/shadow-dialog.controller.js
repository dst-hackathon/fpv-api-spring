(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('ShadowDialogController', ShadowDialogController);

    ShadowDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Shadow', 'Employee', 'Desk'];

    function ShadowDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Shadow, Employee, Desk) {
        var vm = this;

        vm.shadow = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.desks = Desk.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.shadow.id !== null) {
                Shadow.update(vm.shadow, onSaveSuccess, onSaveError);
            } else {
                Shadow.save(vm.shadow, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fpvApp:shadowUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
