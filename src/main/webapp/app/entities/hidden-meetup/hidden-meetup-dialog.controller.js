(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('HiddenMeetupDialogController', HiddenMeetupDialogController);

    HiddenMeetupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HiddenMeetup', 'MeetupGroup'];

    function HiddenMeetupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HiddenMeetup, MeetupGroup) {
        var vm = this;

        vm.hiddenMeetup = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.hiddenMeetup.id !== null) {
                HiddenMeetup.update(vm.hiddenMeetup, onSaveSuccess, onSaveError);
            } else {
                HiddenMeetup.save(vm.hiddenMeetup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('meetupApp:hiddenMeetupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
