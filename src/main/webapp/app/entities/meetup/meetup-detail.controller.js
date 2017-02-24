(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('MeetupDetailController', MeetupDetailController);

    MeetupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Meetup', 'MeetupGroup'];

    function MeetupDetailController($scope, $rootScope, $stateParams, previousState, entity, Meetup, MeetupGroup) {
        var vm = this;

        vm.meetup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('meetupApp:meetupUpdate', function(event, result) {
            vm.meetup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
