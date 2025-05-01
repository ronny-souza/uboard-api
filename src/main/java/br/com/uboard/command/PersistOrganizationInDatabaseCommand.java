package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.User;
import br.com.uboard.core.model.operations.PersistOrganizationInDatabaseForm;
import br.com.uboard.core.repository.CredentialRepository;
import br.com.uboard.core.repository.OrganizationRepository;
import br.com.uboard.core.repository.UserRepository;
import br.com.uboard.exception.CredentialNotFoundException;
import br.com.uboard.exception.UboardApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Lazy
@Component
public class PersistOrganizationInDatabaseCommand implements TaskStageCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistOrganizationInDatabaseCommand.class);
    private final CustomObjectMapper customObjectMapper;
    private final OrganizationRepository organizationRepository;
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;

    public PersistOrganizationInDatabaseCommand(CustomObjectMapper customObjectMapper,
                                                OrganizationRepository organizationRepository,
                                                CredentialRepository credentialRepository,
                                                UserRepository userRepository) {
        this.customObjectMapper = customObjectMapper;
        this.organizationRepository = organizationRepository;
        this.credentialRepository = credentialRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Optional<Object> execute(String payload) throws UboardApplicationException {
        LOGGER.info("Persisting organization in database...");
        PersistOrganizationInDatabaseForm form = this.customObjectMapper.fromJson(
                payload, PersistOrganizationInDatabaseForm.class
        );

        Credential credential = this.credentialRepository.findByUuidAndUserUuid(form.credential(), form.user())
                .orElseThrow(() -> new CredentialNotFoundException("Credential is not found"));

        User user = this.userRepository.getUserByUuid(form.user());

        this.organizationRepository.save(new Organization(form, credential, user));
        return Optional.empty();
    }
}
