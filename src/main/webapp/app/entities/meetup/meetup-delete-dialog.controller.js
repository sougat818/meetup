(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('MeetupDeleteController',MeetupDeleteController);

    MeetupDeleteController.$inject = ['$uibModalInstance', 'entity', 'Meetup'];

    function MeetupDeleteController($uibModalInstance, entity, Meetup) {
        var vm = this;

        vm.meetup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Meetup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
