'use strict';

describe('Controller Tests', function() {

    describe('Building Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBuilding, MockPlan, MockFloor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBuilding = jasmine.createSpy('MockBuilding');
            MockPlan = jasmine.createSpy('MockPlan');
            MockFloor = jasmine.createSpy('MockFloor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Building': MockBuilding,
                'Plan': MockPlan,
                'Floor': MockFloor
            };
            createController = function() {
                $injector.get('$controller')("BuildingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fpvApp:buildingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
