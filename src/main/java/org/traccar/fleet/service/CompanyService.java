package org.traccar.fleet.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.traccar.fleet.domain.Company;
import org.traccar.fleet.repository.CompanyRepository;
import org.traccar.fleet.web.rest.CompanyResource;

@Service
@Transactional(readOnly = true)
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);
    
    @Inject
    private CompanyRepository companyRepository;
    
    public Company findFirstByDomain(String domain) {
    	return companyRepository.findFirstByDomain(domain);
    }

}
