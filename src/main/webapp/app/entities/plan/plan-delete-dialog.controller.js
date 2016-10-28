(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('PlanDeleteController',PlanDeleteController);

    PlanDeleteController.$inject = ['$uibModalInstance', 'entity', 'Plan'];

    function PlanDeleteController($uibModalInstance, entity, Plan) {
        var vm = this;

        vm.plan = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Plan.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
