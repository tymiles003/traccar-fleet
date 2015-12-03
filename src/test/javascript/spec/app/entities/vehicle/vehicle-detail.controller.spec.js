'use strict';

describe('Vehicle Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockVehicle, MockCompany;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockVehicle = jasmine.createSpy('MockVehicle');
        MockCompany = jasmine.createSpy('MockCompany');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Vehicle': MockVehicle,
            'Company': MockCompany
        };
        createController = function() {
            $injector.get('$controller')("VehicleDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'fleetApp:vehicleUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
