(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mutura-services', {
            parent: 'entity',
            url: '/mutura-services',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'muturamaApp.muturaServices.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mutura-services/mutura-services.html',
                    controller: 'MuturaServicesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('muturaServices');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('mutura-services-detail', {
            parent: 'mutura-services',
            url: '/mutura-services/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'muturamaApp.muturaServices.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mutura-services/mutura-services-detail.html',
                    controller: 'MuturaServicesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('muturaServices');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MuturaServices', function($stateParams, MuturaServices) {
                    return MuturaServices.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mutura-services',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mutura-services-detail.edit', {
            parent: 'mutura-services-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mutura-services/mutura-services-dialog.html',
                    controller: 'MuturaServicesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MuturaServices', function(MuturaServices) {
                            return MuturaServices.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mutura-services.new', {
            parent: 'mutura-services',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mutura-services/mutura-services-dialog.html',
                    controller: 'MuturaServicesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mutura-services', null, { reload: 'mutura-services' });
                }, function() {
                    $state.go('mutura-services');
                });
            }]
        })
        .state('mutura-services.edit', {
            parent: 'mutura-services',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mutura-services/mutura-services-dialog.html',
                    controller: 'MuturaServicesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MuturaServices', function(MuturaServices) {
                            return MuturaServices.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mutura-services', null, { reload: 'mutura-services' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mutura-services.delete', {
            parent: 'mutura-services',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mutura-services/mutura-services-delete-dialog.html',
                    controller: 'MuturaServicesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MuturaServices', function(MuturaServices) {
                            return MuturaServices.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mutura-services', null, { reload: 'mutura-services' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
