package org.traccar.fleet.web.rest;

import org.traccar.fleet.Application;
import org.traccar.fleet.domain.Vehicle;
import org.traccar.fleet.repository.VehicleRepository;
import org.traccar.fleet.web.rest.dto.VehicleDTO;
import org.traccar.fleet.web.rest.mapper.VehicleMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the VehicleResource REST controller.
 *
 * @see VehicleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VehicleResourceIntTest {

    private static final String DEFAULT_PLATE = "AAAAA";
    private static final String UPDATED_PLATE = "BBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final Long DEFAULT_EXTERNAL_ID = 1L;
    private static final Long UPDATED_EXTERNAL_ID = 2L;

    @Inject
    private VehicleRepository vehicleRepository;

    @Inject
    private VehicleMapper vehicleMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVehicleMockMvc;

    private Vehicle vehicle;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VehicleResource vehicleResource = new VehicleResource();
        ReflectionTestUtils.setField(vehicleResource, "vehicleRepository", vehicleRepository);
        ReflectionTestUtils.setField(vehicleResource, "vehicleMapper", vehicleMapper);
        this.restVehicleMockMvc = MockMvcBuilders.standaloneSetup(vehicleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        vehicle = new Vehicle();
        vehicle.setPlate(DEFAULT_PLATE);
        vehicle.setActivated(DEFAULT_ACTIVATED);
        vehicle.setExternalId(DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void createVehicle() throws Exception {
        int databaseSizeBeforeCreate = vehicleRepository.findAll().size();

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.vehicleToVehicleDTO(vehicle);

        restVehicleMockMvc.perform(post("/api/vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vehicleDTO)))
                .andExpect(status().isCreated());

        // Validate the Vehicle in the database
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(databaseSizeBeforeCreate + 1);
        Vehicle testVehicle = vehicles.get(vehicles.size() - 1);
        assertThat(testVehicle.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testVehicle.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testVehicle.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void checkPlateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRepository.findAll().size();
        // set the field null
        vehicle.setPlate(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.vehicleToVehicleDTO(vehicle);

        restVehicleMockMvc.perform(post("/api/vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vehicleDTO)))
                .andExpect(status().isBadRequest());

        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRepository.findAll().size();
        // set the field null
        vehicle.setActivated(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.vehicleToVehicleDTO(vehicle);

        restVehicleMockMvc.perform(post("/api/vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vehicleDTO)))
                .andExpect(status().isBadRequest());

        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVehicles() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicles
        restVehicleMockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
                .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE.toString())))
                .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
                .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID.intValue())));
    }

    @Test
    @Transactional
    public void getVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get the vehicle
        restVehicleMockMvc.perform(get("/api/vehicles/{id}", vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(vehicle.getId().intValue()))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVehicle() throws Exception {
        // Get the vehicle
        restVehicleMockMvc.perform(get("/api/vehicles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

		int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

        // Update the vehicle
        vehicle.setPlate(UPDATED_PLATE);
        vehicle.setActivated(UPDATED_ACTIVATED);
        vehicle.setExternalId(UPDATED_EXTERNAL_ID);
        VehicleDTO vehicleDTO = vehicleMapper.vehicleToVehicleDTO(vehicle);

        restVehicleMockMvc.perform(put("/api/vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vehicleDTO)))
                .andExpect(status().isOk());

        // Validate the Vehicle in the database
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(databaseSizeBeforeUpdate);
        Vehicle testVehicle = vehicles.get(vehicles.size() - 1);
        assertThat(testVehicle.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testVehicle.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testVehicle.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void deleteVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

		int databaseSizeBeforeDelete = vehicleRepository.findAll().size();

        // Get the vehicle
        restVehicleMockMvc.perform(delete("/api/vehicles/{id}", vehicle.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
