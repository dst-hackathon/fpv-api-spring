'use strict';

describe('Controller Tests', function() {

    describe('DeskAssignment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDeskAssignment, MockDesk, MockPlan, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDeskAssignment = jasmine.createSpy('MockDeskAssignment');
            MockDesk = jasmine.createSpy('MockDesk');
            MockPlan = jasmine.createSpy('MockPlan');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DeskAssignment': MockDeskAssignment,
                'Desk': MockDesk,
                'Plan': MockPlan,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("DeskAssignmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fpvApp:deskAssignmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
