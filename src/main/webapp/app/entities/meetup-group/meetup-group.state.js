(function() {
    'use strict';

    angular
        .module('meetupApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('meetup-group', {
            parent: 'entity',
            url: '/meetup-group?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MeetupGroups'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meetup-group/meetup-groups.html',
                    controller: 'MeetupGroupController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('meetup-group-detail', {
            parent: 'meetup-group',
            url: '/meetup-group/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MeetupGroup'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meetup-group/meetup-group-detail.html',
                    controller: 'MeetupGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MeetupGroup', function($stateParams, MeetupGroup) {
                    return MeetupGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'meetup-group',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('meetup-group-detail.edit', {
            parent: 'meetup-group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meetup-group/meetup-group-dialog.html',
                    controller: 'MeetupGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeetupGroup', function(MeetupGroup) {
                            return MeetupGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meetup-group.new', {
            parent: 'meetup-group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meetup-group/meetup-group-dialog.html',
                    controller: 'MeetupGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                groupId: null,
                                groupName: null,
                                groupURL: null,
                                blocked: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('meetup-group', null, { reload: 'meetup-group' });
                }, function() {
                    $state.go('meetup-group');
                });
            }]
        })
        .state('meetup-group.edit', {
            parent: 'meetup-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meetup-group/meetup-group-dialog.html',
                    controller: 'MeetupGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeetupGroup', function(MeetupGroup) {
                            return MeetupGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meetup-group', null, { reload: 'meetup-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meetup-group.delete', {
            parent: 'meetup-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meetup-group/meetup-group-delete-dialog.html',
                    controller: 'MeetupGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MeetupGroup', function(MeetupGroup) {
                            return MeetupGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meetup-group', null, { reload: 'meetup-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
