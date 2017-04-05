'use strict';

describe('Controller Tests', function() {

    describe('HiddenMeetup Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHiddenMeetup, MockMeetupGroup;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHiddenMeetup = jasmine.createSpy('MockHiddenMeetup');
            MockMeetupGroup = jasmine.createSpy('MockMeetupGroup');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'HiddenMeetup': MockHiddenMeetup,
                'MeetupGroup': MockMeetupGroup
            };
            createController = function() {
                $injector.get('$controller')("HiddenMeetupDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'meetupApp:hiddenMeetupUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
