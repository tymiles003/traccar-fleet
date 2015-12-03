package org.traccar.fleet.web.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.traccar.fleet.domain.Company;
import org.traccar.fleet.security.UserNotActivatedException;
import org.traccar.fleet.service.CompanyService;


public abstract class FleetResource {

    @Inject
    private CompanyService companyService;
    
    public Company obterOrganizacao(HttpServletRequest request) {
        String subdomain = "ceasape";// organizacaoService.getSubDomain(request);
        Company company = companyService.findFirstByDomain(subdomain);
        if (company == null || !company.getActivated()) {
//            throw new CustomParameterizedException("Organização {0} não encontrada ou está inativa", subdomain);
            // TODO: pensar e existe uma forma melhor. Lancei UserNotActivatedException pra bloquear o usuário no frontend
            throw new UserNotActivatedException("Subdomínio não existe ou está inativo");
        }
        return company;
    }
}
