(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('MeetupGroupDialogController', MeetupGroupDialogController);

    MeetupGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MeetupGroup', 'Meetup'];

    function MeetupGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MeetupGroup, Meetup) {
        var vm = this;

        vm.meetupGroup = entity;
        vm.clear = clear;
        vm.save = save;
        vm.meetups = Meetup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.meetupGroup.id !== null) {
                MeetupGroup.update(vm.meetupGroup, onSaveSuccess, onSaveError);
            } else {
                MeetupGroup.save(vm.meetupGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('meetupApp:meetupGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
