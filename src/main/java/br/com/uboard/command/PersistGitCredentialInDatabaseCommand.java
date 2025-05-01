package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.User;
import br.com.uboard.core.model.operations.PersistGitCredentialInDatabaseForm;
import br.com.uboard.core.repository.CredentialRepository;
import br.com.uboard.core.repository.UserRepository;
import br.com.uboard.exception.UboardApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Lazy
@Component
public class PersistGitCredentialInDatabaseCommand implements TaskStageCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistGitCredentialInDatabaseCommand.class);
    private final CustomObjectMapper customObjectMapper;
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;

    public PersistGitCredentialInDatabaseCommand(CustomObjectMapper customObjectMapper,
                                                 CredentialRepository credentialRepository,
                                                 UserRepository userRepository) {
        this.customObjectMapper = customObjectMapper;
        this.credentialRepository = credentialRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Optional<Object> execute(String payload) throws UboardApplicationException {
        LOGGER.info("Persisting credential in database...");
        PersistGitCredentialInDatabaseForm form = this.customObjectMapper.fromJson(
                payload, PersistGitCredentialInDatabaseForm.class
        );

        User user = this.userRepository.getUserByUuid(form.userIdentifier());

        this.credentialRepository.save(new Credential(form, user));
        return Optional.empty();
    }
}
