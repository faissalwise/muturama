(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agent-list', {
            parent: 'entity',
            url: '/agent-list?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'muturamaApp.agentList.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent-list/agent-lists.html',
                    controller: 'AgentListController',
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
                    $translatePartialLoader.addPart('agentList');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('agent-list-detail', {
            parent: 'agent-list',
            url: '/agent-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'muturamaApp.agentList.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent-list/agent-list-detail.html',
                    controller: 'AgentListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agentList');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AgentList', function($stateParams, AgentList) {
                    return AgentList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'agent-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('agent-list-detail.edit', {
            parent: 'agent-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-list/agent-list-dialog.html',
                    controller: 'AgentListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgentList', function(AgentList) {
                            return AgentList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-list.new', {
            parent: 'agent-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-list/agent-list-dialog.html',
                    controller: 'AgentListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cin: null,
                                nom: null,
                                prenom: null,
                                address: null,
                                lat: null,
                                lon: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('agent-list', null, { reload: 'agent-list' });
                }, function() {
                    $state.go('agent-list');
                });
            }]
        })
        .state('agent-list.edit', {
            parent: 'agent-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-list/agent-list-dialog.html',
                    controller: 'AgentListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgentList', function(AgentList) {
                            return AgentList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-list', null, { reload: 'agent-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-list.delete', {
            parent: 'agent-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-list/agent-list-delete-dialog.html',
                    controller: 'AgentListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AgentList', function(AgentList) {
                            return AgentList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-list', null, { reload: 'agent-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
