(function() {
    'use strict';

    angular
        .module('fpvApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('floor', {
            parent: 'entity',
            url: '/floor?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.floor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/floor/floors.html',
                    controller: 'FloorController',
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
                    $translatePartialLoader.addPart('floor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('floor-detail', {
            parent: 'entity',
            url: '/floor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.floor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/floor/floor-detail.html',
                    controller: 'FloorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('floor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Floor', function($stateParams, Floor) {
                    return Floor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'floor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('floor-detail.edit', {
            parent: 'floor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/floor/floor-dialog.html',
                    controller: 'FloorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Floor', function(Floor) {
                            return Floor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('floor.new', {
            parent: 'floor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/floor/floor-dialog.html',
                    controller: 'FloorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('floor', null, { reload: 'floor' });
                }, function() {
                    $state.go('floor');
                });
            }]
        })
        .state('floor.edit', {
            parent: 'floor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/floor/floor-dialog.html',
                    controller: 'FloorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Floor', function(Floor) {
                            return Floor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('floor', null, { reload: 'floor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('floor.delete', {
            parent: 'floor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/floor/floor-delete-dialog.html',
                    controller: 'FloorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Floor', function(Floor) {
                            return Floor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('floor', null, { reload: 'floor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
