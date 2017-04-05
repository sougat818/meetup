(function() {
    'use strict';

    angular
        .module('meetupApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hidden-meetup', {
            parent: 'entity',
            url: '/hidden-meetup',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'HiddenMeetups'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hidden-meetup/hidden-meetups.html',
                    controller: 'HiddenMeetupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('hidden-meetup-detail', {
            parent: 'hidden-meetup',
            url: '/hidden-meetup/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'HiddenMeetup'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hidden-meetup/hidden-meetup-detail.html',
                    controller: 'HiddenMeetupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'HiddenMeetup', function($stateParams, HiddenMeetup) {
                    return HiddenMeetup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hidden-meetup',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hidden-meetup-detail.edit', {
            parent: 'hidden-meetup-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hidden-meetup/hidden-meetup-dialog.html',
                    controller: 'HiddenMeetupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HiddenMeetup', function(HiddenMeetup) {
                            return HiddenMeetup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hidden-meetup.new', {
            parent: 'hidden-meetup',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hidden-meetup/hidden-meetup-dialog.html',
                    controller: 'HiddenMeetupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                meetupId: null,
                                meetupName: null,
                                meetupURL: null,
                                meetupGoingStatus: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hidden-meetup', null, { reload: 'hidden-meetup' });
                }, function() {
                    $state.go('hidden-meetup');
                });
            }]
        })
        .state('hidden-meetup.edit', {
            parent: 'hidden-meetup',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hidden-meetup/hidden-meetup-dialog.html',
                    controller: 'HiddenMeetupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HiddenMeetup', function(HiddenMeetup) {
                            return HiddenMeetup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hidden-meetup', null, { reload: 'hidden-meetup' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hidden-meetup.delete', {
            parent: 'hidden-meetup',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hidden-meetup/hidden-meetup-delete-dialog.html',
                    controller: 'HiddenMeetupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HiddenMeetup', function(HiddenMeetup) {
                            return HiddenMeetup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hidden-meetup', null, { reload: 'hidden-meetup' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
