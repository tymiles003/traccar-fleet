package org.traccar.fleet.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.traccar.fleet.domain.Vehicle;
import org.traccar.fleet.repository.VehicleRepository;
import org.traccar.fleet.web.rest.util.HeaderUtil;
import org.traccar.fleet.web.rest.util.PaginationUtil;
import org.traccar.fleet.web.rest.dto.VehicleDTO;
import org.traccar.fleet.web.rest.mapper.VehicleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Vehicle.
 */
@RestController
@RequestMapping("/api")
public class VehicleResource extends FleetResource {

    private final Logger log = LoggerFactory.getLogger(VehicleResource.class);

    @Inject
    private VehicleRepository vehicleRepository;

    @Inject
    private VehicleMapper vehicleMapper;

    /**
     * POST  /vehicles -> Create a new vehicle.
     */
    @RequestMapping(value = "/vehicles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) throws URISyntaxException {
        log.debug("REST request to save Vehicle : {}", vehicleDTO);
        if (vehicleDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new vehicle cannot already have an ID").body(null);
        }
        Vehicle vehicle = vehicleMapper.vehicleDTOToVehicle(vehicleDTO);
        Vehicle result = vehicleRepository.save(vehicle);
        return ResponseEntity.created(new URI("/api/vehicles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vehicle", result.getId().toString()))
            .body(vehicleMapper.vehicleToVehicleDTO(result));
    }

    /**
     * PUT  /vehicles -> Updates an existing vehicle.
     */
    @RequestMapping(value = "/vehicles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VehicleDTO> updateVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) throws URISyntaxException {
        log.debug("REST request to update Vehicle : {}", vehicleDTO);
        if (vehicleDTO.getId() == null) {
            return createVehicle(vehicleDTO);
        }
        Vehicle vehicle = vehicleMapper.vehicleDTOToVehicle(vehicleDTO);
        Vehicle result = vehicleRepository.save(vehicle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vehicle", vehicleDTO.getId().toString()))
            .body(vehicleMapper.vehicleToVehicleDTO(result));
    }

    /**
     * GET  /vehicles -> get all the vehicles.
     */
    @RequestMapping(value = "/vehicles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(Pageable pageable)
        throws URISyntaxException {
        Page<Vehicle> page = vehicleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vehicles");
        return new ResponseEntity<>(page.getContent().stream()
            .map(vehicleMapper::vehicleToVehicleDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /vehicles/:id -> get the "id" vehicle.
     */
    @RequestMapping(value = "/vehicles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable Long id) {
        log.debug("REST request to get Vehicle : {}", id);
        return Optional.ofNullable(vehicleRepository.findOne(id))
            .map(vehicleMapper::vehicleToVehicleDTO)
            .map(vehicleDTO -> new ResponseEntity<>(
                vehicleDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vehicles/:id -> delete the "id" vehicle.
     */
    @RequestMapping(value = "/vehicles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        log.debug("REST request to delete Vehicle : {}", id);
        vehicleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vehicle", id.toString())).build();
    }
}
