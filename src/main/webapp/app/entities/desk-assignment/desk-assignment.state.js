(function() {
    'use strict';

    angular
        .module('fpvApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('desk-assignment', {
            parent: 'entity',
            url: '/desk-assignment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.deskAssignment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/desk-assignment/desk-assignments.html',
                    controller: 'DeskAssignmentController',
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
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deskAssignment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('desk-assignment-detail', {
            parent: 'entity',
            url: '/desk-assignment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.deskAssignment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/desk-assignment/desk-assignment-detail.html',
                    controller: 'DeskAssignmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deskAssignment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DeskAssignment', function($stateParams, DeskAssignment) {
                    return DeskAssignment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'desk-assignment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('desk-assignment-detail.edit', {
            parent: 'desk-assignment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/desk-assignment/desk-assignment-dialog.html',
                    controller: 'DeskAssignmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DeskAssignment', function(DeskAssignment) {
                            return DeskAssignment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('desk-assignment.new', {
            parent: 'desk-assignment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/desk-assignment/desk-assignment-dialog.html',
                    controller: 'DeskAssignmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('desk-assignment', null, { reload: 'desk-assignment' });
                }, function() {
                    $state.go('desk-assignment');
                });
            }]
        })
        .state('desk-assignment.edit', {
            parent: 'desk-assignment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/desk-assignment/desk-assignment-dialog.html',
                    controller: 'DeskAssignmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DeskAssignment', function(DeskAssignment) {
                            return DeskAssignment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('desk-assignment', null, { reload: 'desk-assignment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('desk-assignment.delete', {
            parent: 'desk-assignment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/desk-assignment/desk-assignment-delete-dialog.html',
                    controller: 'DeskAssignmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DeskAssignment', function(DeskAssignment) {
                            return DeskAssignment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('desk-assignment', null, { reload: 'desk-assignment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
