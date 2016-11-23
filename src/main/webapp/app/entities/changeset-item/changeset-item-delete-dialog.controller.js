(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('ChangesetItemDeleteController',ChangesetItemDeleteController);

    ChangesetItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChangesetItem'];

    function ChangesetItemDeleteController($uibModalInstance, entity, ChangesetItem) {
        var vm = this;

        vm.changesetItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChangesetItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
