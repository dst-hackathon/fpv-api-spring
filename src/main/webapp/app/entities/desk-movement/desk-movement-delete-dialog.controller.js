(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('DeskMovementDeleteController',DeskMovementDeleteController);

    DeskMovementDeleteController.$inject = ['$uibModalInstance', 'entity', 'DeskMovement'];

    function DeskMovementDeleteController($uibModalInstance, entity, DeskMovement) {
        var vm = this;

        vm.deskMovement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DeskMovement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
