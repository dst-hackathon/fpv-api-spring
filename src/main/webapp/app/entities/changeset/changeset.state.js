(function() {
    'use strict';

    angular
        .module('fpvApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('changeset', {
            parent: 'entity',
            url: '/changeset?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.changeset.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/changeset/changesets.html',
                    controller: 'ChangesetController',
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
                    $translatePartialLoader.addPart('changeset');
                    $translatePartialLoader.addPart('changesetStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('changeset-detail', {
            parent: 'entity',
            url: '/changeset/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fpvApp.changeset.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/changeset/changeset-detail.html',
                    controller: 'ChangesetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('changeset');
                    $translatePartialLoader.addPart('changesetStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Changeset', function($stateParams, Changeset) {
                    return Changeset.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'changeset',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('changeset-detail.edit', {
            parent: 'changeset-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/changeset/changeset-dialog.html',
                    controller: 'ChangesetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Changeset', function(Changeset) {
                            return Changeset.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('changeset.new', {
            parent: 'changeset',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/changeset/changeset-dialog.html',
                    controller: 'ChangesetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                effectiveDate: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('changeset', null, { reload: 'changeset' });
                }, function() {
                    $state.go('changeset');
                });
            }]
        })
        .state('changeset.edit', {
            parent: 'changeset',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/changeset/changeset-dialog.html',
                    controller: 'ChangesetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Changeset', function(Changeset) {
                            return Changeset.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('changeset', null, { reload: 'changeset' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('changeset.delete', {
            parent: 'changeset',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/changeset/changeset-delete-dialog.html',
                    controller: 'ChangesetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Changeset', function(Changeset) {
                            return Changeset.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('changeset', null, { reload: 'changeset' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
