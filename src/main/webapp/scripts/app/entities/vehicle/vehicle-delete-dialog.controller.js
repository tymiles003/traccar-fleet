'use strict';

angular.module('fleetApp')
	.controller('VehicleDeleteController', function($scope, $modalInstance, entity, Vehicle) {

        $scope.vehicle = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Vehicle.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });