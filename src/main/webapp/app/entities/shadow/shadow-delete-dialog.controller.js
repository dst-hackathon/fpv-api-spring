(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('ShadowDeleteController',ShadowDeleteController);

    ShadowDeleteController.$inject = ['$uibModalInstance', 'entity', 'Shadow'];

    function ShadowDeleteController($uibModalInstance, entity, Shadow) {
        var vm = this;

        vm.shadow = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Shadow.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
