(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('MeetupGroupDeleteController',MeetupGroupDeleteController);

    MeetupGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'MeetupGroup'];

    function MeetupGroupDeleteController($uibModalInstance, entity, MeetupGroup) {
        var vm = this;

        vm.meetupGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MeetupGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
