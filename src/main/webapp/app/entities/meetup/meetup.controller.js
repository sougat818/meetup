(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('MeetupController', MeetupController);

    MeetupController.$inject = ['Meetup'];

    function MeetupController(Meetup) {

        var vm = this;

        vm.meetups = [];

        loadAll();

        function loadAll() {
            Meetup.query(function(result) {
                vm.meetups = result;
                vm.searchQuery = null;
            });
        }
    }
})();
