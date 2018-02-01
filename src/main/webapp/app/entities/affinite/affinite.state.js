(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('affinite', {
            parent: 'entity',
            url: '/affinite?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'muturamaApp.affinite.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/affinite/affinites.html',
                    controller: 'AffiniteController',
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
                    $translatePartialLoader.addPart('affinite');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('affinite-detail', {
            parent: 'affinite',
            url: '/affinite/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'muturamaApp.affinite.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/affinite/affinite-detail.html',
                    controller: 'AffiniteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('affinite');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Affinite', function($stateParams, Affinite) {
                    return Affinite.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'affinite',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('affinite-detail.edit', {
            parent: 'affinite-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/affinite/affinite-dialog.html',
                    controller: 'AffiniteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Affinite', function(Affinite) {
                            return Affinite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('affinite.new', {
            parent: 'affinite',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/affinite/affinite-dialog.html',
                    controller: 'AffiniteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('affinite', null, { reload: 'affinite' });
                }, function() {
                    $state.go('affinite');
                });
            }]
        })
        .state('affinite.edit', {
            parent: 'affinite',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/affinite/affinite-dialog.html',
                    controller: 'AffiniteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Affinite', function(Affinite) {
                            return Affinite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('affinite', null, { reload: 'affinite' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('affinite.delete', {
            parent: 'affinite',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/affinite/affinite-delete-dialog.html',
                    controller: 'AffiniteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Affinite', function(Affinite) {
                            return Affinite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('affinite', null, { reload: 'affinite' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
