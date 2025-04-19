package br.com.uboard.core.service;

import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.filters.CredentialFiltersDTO;
import br.com.uboard.core.model.transport.CredentialDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.CredentialRepository;
import br.com.uboard.core.repository.specification.CredentialSpecification;
import br.com.uboard.core.repository.specification.builder.CredentialSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ListCredentialsService {

    private final CredentialRepository credentialRepository;

    public ListCredentialsService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public Page<CredentialDTO> listCredentialsAsPage(Pageable pageable, SessionUserDTO sessionUserDTO, CredentialFiltersDTO filters) {
        Specification<Credential> specifications = CredentialSpecificationBuilder.builder()
                .withSpecification(CredentialSpecification.belongsToUser(sessionUserDTO.id()))
                .withFilters(filters)
                .build();
        return this.credentialRepository.findAll(specifications, pageable).map(CredentialDTO::new);
    }
}
