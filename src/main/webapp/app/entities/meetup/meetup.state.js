(function() {
    'use strict';

    angular
        .module('meetupApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('meetup', {
            parent: 'entity',
            url: '/meetup',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Meetups'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meetup/meetups.html',
                    controller: 'MeetupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('meetup-detail', {
            parent: 'meetup',
            url: '/meetup/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Meetup'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meetup/meetup-detail.html',
                    controller: 'MeetupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Meetup', function($stateParams, Meetup) {
                    return Meetup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'meetup',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('meetup-detail.edit', {
            parent: 'meetup-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meetup/meetup-dialog.html',
                    controller: 'MeetupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Meetup', function(Meetup) {
                            return Meetup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meetup.new', {
            parent: 'meetup',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meetup/meetup-dialog.html',
                    controller: 'MeetupDialogController',
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
                    $state.go('meetup', null, { reload: 'meetup' });
                }, function() {
                    $state.go('meetup');
                });
            }]
        })
        .state('meetup.edit', {
            parent: 'meetup',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meetup/meetup-dialog.html',
                    controller: 'MeetupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Meetup', function(Meetup) {
                            return Meetup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meetup', null, { reload: 'meetup' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meetup.delete', {
            parent: 'meetup',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meetup/meetup-delete-dialog.html',
                    controller: 'MeetupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Meetup', function(Meetup) {
                            return Meetup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meetup', null, { reload: 'meetup' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
