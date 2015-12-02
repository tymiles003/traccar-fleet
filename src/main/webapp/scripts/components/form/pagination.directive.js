/* globals $ */
'use strict';

angular.module('fleetApp')
    .directive('fleetAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
