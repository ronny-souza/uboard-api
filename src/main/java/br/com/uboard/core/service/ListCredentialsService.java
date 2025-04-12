package br.com.uboard.core.service;

import br.com.uboard.core.model.transport.CredentialDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.CredentialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListCredentialsService {

    private final CredentialRepository credentialRepository;

    public ListCredentialsService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public Page<CredentialDTO> listCredentialsAsPage(Pageable pageable, SessionUserDTO sessionUserDTO) {
        return this.credentialRepository.findAllByUserUuid(sessionUserDTO.id(), pageable).map(CredentialDTO::new);
    }
}
