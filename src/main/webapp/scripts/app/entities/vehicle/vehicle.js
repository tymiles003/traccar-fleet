'use strict';

angular.module('fleetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('vehicle', {
                parent: 'entity',
                url: '/vehicles',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'fleetApp.vehicle.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/vehicle/vehicles.html',
                        controller: 'VehicleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('vehicle');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('vehicle.detail', {
                parent: 'entity',
                url: '/vehicle/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'fleetApp.vehicle.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/vehicle/vehicle-detail.html',
                        controller: 'VehicleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('vehicle');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Vehicle', function($stateParams, Vehicle) {
                        return Vehicle.get({id : $stateParams.id});
                    }]
                }
            })
            .state('vehicle.new', {
                parent: 'vehicle',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/vehicle/vehicle-dialog.html',
                        controller: 'VehicleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    plate: null,
                                    activated: false,
                                    externalId: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('vehicle', null, { reload: true });
                    }, function() {
                        $state.go('vehicle');
                    })
                }]
            })
            .state('vehicle.edit', {
                parent: 'vehicle',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/vehicle/vehicle-dialog.html',
                        controller: 'VehicleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Vehicle', function(Vehicle) {
                                return Vehicle.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('vehicle', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('vehicle.delete', {
                parent: 'vehicle',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/vehicle/vehicle-delete-dialog.html',
                        controller: 'VehicleDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Vehicle', function(Vehicle) {
                                return Vehicle.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('vehicle', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
