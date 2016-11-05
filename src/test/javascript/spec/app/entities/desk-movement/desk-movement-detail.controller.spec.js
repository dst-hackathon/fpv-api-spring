'use strict';

describe('Controller Tests', function() {

    describe('DeskMovement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDeskMovement, MockEmployee, MockDesk, MockPlan;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDeskMovement = jasmine.createSpy('MockDeskMovement');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockDesk = jasmine.createSpy('MockDesk');
            MockPlan = jasmine.createSpy('MockPlan');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DeskMovement': MockDeskMovement,
                'Employee': MockEmployee,
                'Desk': MockDesk,
                'Plan': MockPlan
            };
            createController = function() {
                $injector.get('$controller')("DeskMovementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fpvApp:deskMovementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
