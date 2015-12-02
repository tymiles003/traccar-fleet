'use strict';

angular.module('fleetApp')
	.controller('CompanyDeleteController', function($scope, $modalInstance, entity, Company) {

        $scope.company = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Company.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });