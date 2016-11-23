'use strict';

describe('Controller Tests', function() {

    describe('Changeset Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChangeset, MockPlan, MockChangesetItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChangeset = jasmine.createSpy('MockChangeset');
            MockPlan = jasmine.createSpy('MockPlan');
            MockChangesetItem = jasmine.createSpy('MockChangesetItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Changeset': MockChangeset,
                'Plan': MockPlan,
                'ChangesetItem': MockChangesetItem
            };
            createController = function() {
                $injector.get('$controller')("ChangesetDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fpvApp:changesetUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
