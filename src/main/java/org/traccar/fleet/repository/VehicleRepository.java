package org.traccar.fleet.repository;

import org.traccar.fleet.domain.Vehicle;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vehicle entity.
 */
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

}
