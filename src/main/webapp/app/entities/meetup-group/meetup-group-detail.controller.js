(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('MeetupGroupDetailController', MeetupGroupDetailController);

    MeetupGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MeetupGroup', 'Meetup'];

    function MeetupGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, MeetupGroup, Meetup) {
        var vm = this;

        vm.meetupGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('meetupApp:meetupGroupUpdate', function(event, result) {
            vm.meetupGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
