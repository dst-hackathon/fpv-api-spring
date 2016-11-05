(function() {
    'use strict';

    angular
        .module('fpvApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('shadow', {
            parent: 'entity',
            url: '/shadow?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.shadow.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shadow/shadows.html',
                    controller: 'ShadowController',
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
                    $translatePartialLoader.addPart('shadow');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shadow-detail', {
            parent: 'entity',
            url: '/shadow/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.shadow.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shadow/shadow-detail.html',
                    controller: 'ShadowDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shadow');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Shadow', function($stateParams, Shadow) {
                    return Shadow.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'shadow',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('shadow-detail.edit', {
            parent: 'shadow-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shadow/shadow-dialog.html',
                    controller: 'ShadowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Shadow', function(Shadow) {
                            return Shadow.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shadow.new', {
            parent: 'shadow',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shadow/shadow-dialog.html',
                    controller: 'ShadowDialogController',
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
                    $state.go('shadow', null, { reload: 'shadow' });
                }, function() {
                    $state.go('shadow');
                });
            }]
        })
        .state('shadow.edit', {
            parent: 'shadow',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shadow/shadow-dialog.html',
                    controller: 'ShadowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Shadow', function(Shadow) {
                            return Shadow.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('shadow', null, { reload: 'shadow' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shadow.delete', {
            parent: 'shadow',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shadow/shadow-delete-dialog.html',
                    controller: 'ShadowDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Shadow', function(Shadow) {
                            return Shadow.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('shadow', null, { reload: 'shadow' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
