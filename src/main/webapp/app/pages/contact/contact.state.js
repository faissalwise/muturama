(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider            .state('contact-contact', {
                parent: 'page-sets',
                url: '/pages/contact/contact',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'muturamaApp.contact.title'
                },
        views: {
            'content@': {
                templateUrl: 'app/pages/contact/contact.html',
                    controller: 'contactController',
                    controllerAs: 'vm'
            }
        },
        resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('contact');
                $translatePartialLoader.addPart('global');
                return $translate.refresh();
            }],
            
        }
    })
            .state('contact-aboutUs', {
                parent: 'page-sets',
                url: '/pages/contact/about-us',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'muturamaApp.aboutUs.title'
                },
        views: {
            'content@': {
                templateUrl: 'app/pages/contact/aboutUs.html',
                    controller: 'aboutUsController',
                    controllerAs: 'vm'
            }
        },
        resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('contact');
                $translatePartialLoader.addPart('global');
                return $translate.refresh();
            }],
            
        }
    })
    ;
    }

})();
