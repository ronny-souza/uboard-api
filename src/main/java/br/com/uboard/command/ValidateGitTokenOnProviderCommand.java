package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.external.GitUserInterface;
import br.com.uboard.core.model.operations.ValidateGitTokenOnProviderForm;
import br.com.uboard.core.service.GitService;
import br.com.uboard.exception.UboardApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Lazy
@Component
public class ValidateGitTokenOnProviderCommand implements TaskStageCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateGitTokenOnProviderCommand.class);
    private final CustomObjectMapper customObjectMapper;
    private final GitService gitService;

    public ValidateGitTokenOnProviderCommand(CustomObjectMapper customObjectMapper,
                                             GitService gitService) {
        this.customObjectMapper = customObjectMapper;
        this.gitService = gitService;
    }

    @Override
    public Optional<Object> execute(String payload) throws UboardApplicationException {
        LOGGER.info("Starting token validation at provider...");
        ValidateGitTokenOnProviderForm form = this.customObjectMapper.fromJson(
                payload, ValidateGitTokenOnProviderForm.class
        );

        GitUserInterface currentUser = this.gitService.getCurrentUser(form.url(), form.token(), form.type());
        LOGGER.debug("User {} has been successfully recovered!", currentUser.getUsername());

        return Optional.empty();
    }
}
