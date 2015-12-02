'use strict';

angular.module('fleetApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


