(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('ChangesetDeleteController',ChangesetDeleteController);

    ChangesetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Changeset'];

    function ChangesetDeleteController($uibModalInstance, entity, Changeset) {
        var vm = this;

        vm.changeset = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Changeset.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
