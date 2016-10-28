(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('DeskAssignmentDeleteController',DeskAssignmentDeleteController);

    DeskAssignmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'DeskAssignment'];

    function DeskAssignmentDeleteController($uibModalInstance, entity, DeskAssignment) {
        var vm = this;

        vm.deskAssignment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DeskAssignment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
