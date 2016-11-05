(function() {
    'use strict';

    angular
        .module('fpvApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('desk-movement', {
            parent: 'entity',
            url: '/desk-movement?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.deskMovement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/desk-movement/desk-movements.html',
                    controller: 'DeskMovementController',
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
                    $translatePartialLoader.addPart('deskMovement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('desk-movement-detail', {
            parent: 'entity',
            url: '/desk-movement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.deskMovement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/desk-movement/desk-movement-detail.html',
                    controller: 'DeskMovementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deskMovement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DeskMovement', function($stateParams, DeskMovement) {
                    return DeskMovement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'desk-movement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('desk-movement-detail.edit', {
            parent: 'desk-movement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/desk-movement/desk-movement-dialog.html',
                    controller: 'DeskMovementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DeskMovement', function(DeskMovement) {
                            return DeskMovement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('desk-movement.new', {
            parent: 'desk-movement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/desk-movement/desk-movement-dialog.html',
                    controller: 'DeskMovementDialogController',
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
                    $state.go('desk-movement', null, { reload: 'desk-movement' });
                }, function() {
                    $state.go('desk-movement');
                });
            }]
        })
        .state('desk-movement.edit', {
            parent: 'desk-movement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/desk-movement/desk-movement-dialog.html',
                    controller: 'DeskMovementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DeskMovement', function(DeskMovement) {
                            return DeskMovement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('desk-movement', null, { reload: 'desk-movement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('desk-movement.delete', {
            parent: 'desk-movement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/desk-movement/desk-movement-delete-dialog.html',
                    controller: 'DeskMovementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DeskMovement', function(DeskMovement) {
                            return DeskMovement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('desk-movement', null, { reload: 'desk-movement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
