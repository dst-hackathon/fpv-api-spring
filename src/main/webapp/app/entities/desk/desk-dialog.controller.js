(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('DeskDialogController', DeskDialogController);

    DeskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Desk', 'Floor'];

    function DeskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Desk, Floor) {
        var vm = this;

        vm.desk = entity;
        vm.clear = clear;
        vm.save = save;
        vm.floors = Floor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.desk.id !== null) {
                Desk.update(vm.desk, onSaveSuccess, onSaveError);
            } else {
                Desk.save(vm.desk, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fpvApp:deskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
