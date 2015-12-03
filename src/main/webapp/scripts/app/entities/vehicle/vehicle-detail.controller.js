'use strict';

angular.module('fleetApp')
    .controller('VehicleDetailController', function ($scope, $rootScope, $stateParams, entity, Vehicle, Company) {
        $scope.vehicle = entity;
        $scope.load = function (id) {
            Vehicle.get({id: id}, function(result) {
                $scope.vehicle = result;
            });
        };
        var unsubscribe = $rootScope.$on('fleetApp:vehicleUpdate', function(event, result) {
            $scope.vehicle = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
