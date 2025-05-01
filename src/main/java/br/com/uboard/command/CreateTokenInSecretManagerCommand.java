package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.operations.CreateTokenInSecretManagerForm;
import br.com.uboard.core.service.VaultService;
import br.com.uboard.exception.UboardApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Lazy
@Component
public class CreateTokenInSecretManagerCommand implements TaskStageCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTokenInSecretManagerCommand.class);
    private final CustomObjectMapper customObjectMapper;
    private final VaultService vaultService;

    public CreateTokenInSecretManagerCommand(CustomObjectMapper customObjectMapper,
                                             VaultService vaultService) {
        this.customObjectMapper = customObjectMapper;
        this.vaultService = vaultService;
    }

    @Override
    public Optional<Object> execute(String payload) throws UboardApplicationException {
        LOGGER.info("Starting token registration in secrets manager...");
        CreateTokenInSecretManagerForm form = this.customObjectMapper.fromJson(
                payload, CreateTokenInSecretManagerForm.class
        );

        Map<String, Object> data = Map.of("token", form.token());
        String path = String.format(VaultService.CREDENTIAL_PATH_PATTERN, form.userIdentifier(), form.credentialIdentifier());
        this.vaultService.registerSecretInVaultService(path, data);

        return Optional.empty();
    }
}
