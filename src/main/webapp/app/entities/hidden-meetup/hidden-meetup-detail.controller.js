(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('HiddenMeetupDetailController', HiddenMeetupDetailController);

    HiddenMeetupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HiddenMeetup', 'MeetupGroup'];

    function HiddenMeetupDetailController($scope, $rootScope, $stateParams, previousState, entity, HiddenMeetup, MeetupGroup) {
        var vm = this;

        vm.hiddenMeetup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('meetupApp:hiddenMeetupUpdate', function(event, result) {
            vm.hiddenMeetup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
