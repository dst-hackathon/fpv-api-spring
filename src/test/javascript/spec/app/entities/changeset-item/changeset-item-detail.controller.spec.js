'use strict';

describe('Controller Tests', function() {

    describe('ChangesetItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChangesetItem, MockEmployee, MockDesk, MockChangeset;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChangesetItem = jasmine.createSpy('MockChangesetItem');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockDesk = jasmine.createSpy('MockDesk');
            MockChangeset = jasmine.createSpy('MockChangeset');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ChangesetItem': MockChangesetItem,
                'Employee': MockEmployee,
                'Desk': MockDesk,
                'Changeset': MockChangeset
            };
            createController = function() {
                $injector.get('$controller')("ChangesetItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fpvApp:changesetItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
