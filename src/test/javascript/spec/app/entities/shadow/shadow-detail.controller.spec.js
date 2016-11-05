'use strict';

describe('Controller Tests', function() {

    describe('Shadow Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockShadow, MockEmployee, MockDesk;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockShadow = jasmine.createSpy('MockShadow');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockDesk = jasmine.createSpy('MockDesk');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Shadow': MockShadow,
                'Employee': MockEmployee,
                'Desk': MockDesk
            };
            createController = function() {
                $injector.get('$controller')("ShadowDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fpvApp:shadowUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
