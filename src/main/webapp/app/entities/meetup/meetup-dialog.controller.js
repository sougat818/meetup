(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('MeetupDialogController', MeetupDialogController);

    MeetupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Meetup', 'MeetupGroup'];

    function MeetupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Meetup, MeetupGroup) {
        var vm = this;

        vm.meetup = entity;
        vm.clear = clear;
        vm.save = save;
        vm.meetupgroups = MeetupGroup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.meetup.id !== null) {
                Meetup.update(vm.meetup, onSaveSuccess, onSaveError);
            } else {
                Meetup.save(vm.meetup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('meetupApp:meetupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
