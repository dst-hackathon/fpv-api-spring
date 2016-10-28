(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('DeskDeleteController',DeskDeleteController);

    DeskDeleteController.$inject = ['$uibModalInstance', 'entity', 'Desk'];

    function DeskDeleteController($uibModalInstance, entity, Desk) {
        var vm = this;

        vm.desk = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Desk.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
