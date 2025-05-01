package br.com.uboard.core.service;

import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.filters.OrganizationFiltersDTO;
import br.com.uboard.core.model.transport.OrganizationDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.OrganizationRepository;
import br.com.uboard.core.repository.specification.OrganizationSpecification;
import br.com.uboard.core.repository.specification.builder.OrganizationSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ListOrganizationsService {
    private final OrganizationRepository organizationRepository;

    public ListOrganizationsService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }


    public Page<OrganizationDTO> listOrganizationsAsPage(Pageable pageable,
                                                         SessionUserDTO sessionUserDTO,
                                                         OrganizationFiltersDTO filters) {
        Specification<Organization> specifications = OrganizationSpecificationBuilder.builder()
                .withSpecification(OrganizationSpecification.belongsToUser(sessionUserDTO.id()))
                .withFilters(filters)
                .build();
        return this.organizationRepository.findAll(specifications, pageable).map(OrganizationDTO::new);
    }
}
