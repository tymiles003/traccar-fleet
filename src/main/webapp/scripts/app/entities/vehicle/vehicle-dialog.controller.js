'use strict';

angular.module('fleetApp').controller('VehicleDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Vehicle', 'Company',
        function($scope, $stateParams, $modalInstance, entity, Vehicle, Company) {

        $scope.vehicle = entity;
        $scope.companys = Company.query();
        $scope.load = function(id) {
            Vehicle.get({id : id}, function(result) {
                $scope.vehicle = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('fleetApp:vehicleUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.vehicle.id != null) {
                Vehicle.update($scope.vehicle, onSaveSuccess, onSaveError);
            } else {
                Vehicle.save($scope.vehicle, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
