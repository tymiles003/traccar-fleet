package org.traccar.fleet.web.rest.mapper;

import org.traccar.fleet.domain.*;
import org.traccar.fleet.web.rest.dto.VehicleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Vehicle and its DTO VehicleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VehicleMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    VehicleDTO vehicleToVehicleDTO(Vehicle vehicle);

    @Mapping(source = "companyId", target = "company")
    Vehicle vehicleDTOToVehicle(VehicleDTO vehicleDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
