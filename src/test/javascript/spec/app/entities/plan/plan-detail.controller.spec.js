'use strict';

describe('Controller Tests', function() {

    describe('Plan Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPlan, MockFloor, MockDeskAssignment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPlan = jasmine.createSpy('MockPlan');
            MockFloor = jasmine.createSpy('MockFloor');
            MockDeskAssignment = jasmine.createSpy('MockDeskAssignment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Plan': MockPlan,
                'Floor': MockFloor,
                'DeskAssignment': MockDeskAssignment
            };
            createController = function() {
                $injector.get('$controller')("PlanDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fpvApp:planUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
