(function() {
    'use strict';

    angular
        .module('fpvApp')
        .controller('FloorDialogController', FloorDialogController);

    FloorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Floor', 'Building', 'Desk'];

    function FloorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Floor, Building, Desk) {
        var vm = this;

        vm.floor = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.buildings = Building.query();
        vm.desks = Desk.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.floor.id !== null) {
                Floor.update(vm.floor, onSaveSuccess, onSaveError);
            } else {
                Floor.save(vm.floor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fpvApp:floorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, floor) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        floor.image = base64Data;
                        floor.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
