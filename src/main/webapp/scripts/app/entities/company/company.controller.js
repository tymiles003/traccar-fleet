'use strict';

angular.module('fleetApp')
    .controller('CompanyController', function ($scope, $state, $modal, Company, ParseLinks) {
      
        $scope.companys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Company.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.companys = result;
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
            $scope.company = {
                name: null,
                domain: null,
                activated: false,
                id: null
            };
        };
    });
