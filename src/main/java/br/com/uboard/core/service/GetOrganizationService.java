package br.com.uboard.core.service;

import br.com.uboard.core.model.transport.OrganizationDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.OrganizationRepository;
import br.com.uboard.exception.OrganizationNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetOrganizationService {
    private final OrganizationRepository organizationRepository;

    public GetOrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    // FIX: AJUSTAR CONSULTA PARA PERMITIR MEMBROS DA ORGANIZAÇÃO ACESSAREM TAMBÉM
    public OrganizationDTO getOrganization(String uuid, SessionUserDTO sessionUserDTO)
            throws OrganizationNotFoundException {
        return new OrganizationDTO(this.organizationRepository.getOrganizationByUuidAndUser(uuid, sessionUserDTO.id()));
    }
}
