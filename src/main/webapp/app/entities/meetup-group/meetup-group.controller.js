(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('MeetupGroupController', MeetupGroupController);

    MeetupGroupController.$inject = ['MeetupGroup'];

    function MeetupGroupController(MeetupGroup) {

        var vm = this;

        vm.meetupGroups = [];

        loadAll();

        function loadAll() {
            MeetupGroup.query(function(result) {
                vm.meetupGroups = result;
                vm.searchQuery = null;
            });
        }
    }
})();
