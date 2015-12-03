package org.traccar.fleet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.traccar.fleet.domain.Company;

/**
 * Spring Data JPA repository for the Company entity.
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {

	Company findFirstByDomain(String domain);

}
