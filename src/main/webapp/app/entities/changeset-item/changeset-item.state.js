(function() {
    'use strict';

    angular
        .module('fpvApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('changeset-item', {
            parent: 'entity',
            url: '/changeset-item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.changesetItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/changeset-item/changeset-items.html',
                    controller: 'ChangesetItemController',
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
                    $translatePartialLoader.addPart('changesetItem');
                    $translatePartialLoader.addPart('changesetItemStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('changeset-item-detail', {
            parent: 'entity',
            url: '/changeset-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.changesetItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/changeset-item/changeset-item-detail.html',
                    controller: 'ChangesetItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('changesetItem');
                    $translatePartialLoader.addPart('changesetItemStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ChangesetItem', function($stateParams, ChangesetItem) {
                    return ChangesetItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'changeset-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('changeset-item-detail.edit', {
            parent: 'changeset-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/changeset-item/changeset-item-dialog.html',
                    controller: 'ChangesetItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChangesetItem', function(ChangesetItem) {
                            return ChangesetItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('changeset-item.new', {
            parent: 'changeset-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/changeset-item/changeset-item-dialog.html',
                    controller: 'ChangesetItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('changeset-item', null, { reload: 'changeset-item' });
                }, function() {
                    $state.go('changeset-item');
                });
            }]
        })
        .state('changeset-item.edit', {
            parent: 'changeset-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/changeset-item/changeset-item-dialog.html',
                    controller: 'ChangesetItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChangesetItem', function(ChangesetItem) {
                            return ChangesetItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('changeset-item', null, { reload: 'changeset-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('changeset-item.delete', {
            parent: 'changeset-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/changeset-item/changeset-item-delete-dialog.html',
                    controller: 'ChangesetItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChangesetItem', function(ChangesetItem) {
                            return ChangesetItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('changeset-item', null, { reload: 'changeset-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
