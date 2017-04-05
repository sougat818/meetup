(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('HiddenMeetupDeleteController',HiddenMeetupDeleteController);

    HiddenMeetupDeleteController.$inject = ['$uibModalInstance', 'entity', 'HiddenMeetup'];

    function HiddenMeetupDeleteController($uibModalInstance, entity, HiddenMeetup) {
        var vm = this;

        vm.hiddenMeetup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HiddenMeetup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
