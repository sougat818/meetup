(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('HiddenMeetupController', HiddenMeetupController);

    HiddenMeetupController.$inject = ['HiddenMeetup'];

    function HiddenMeetupController(HiddenMeetup) {

        var vm = this;

        vm.hiddenMeetups = [];

        loadAll();

        function loadAll() {
            HiddenMeetup.query(function(result) {
                vm.hiddenMeetups = result;
                vm.searchQuery = null;
            });
        }
    }
})();
