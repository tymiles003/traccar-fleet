'use strict';

angular.module('fleetApp')
    .controller('VehicleController', function ($scope, $state, $modal, Vehicle, ParseLinks) {
      
        $scope.vehicles = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Vehicle.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.vehicles = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.vehicle = {
                plate: null,
                activated: false,
                externalId: null,
                id: null
            };
        };
    });
