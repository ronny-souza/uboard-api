package br.com.uboard.builder;

import br.com.uboard.common.CustomJsonMapper;
import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.operations.CreateCredentialForm;
import br.com.uboard.exception.UboardApplicationException;
import br.com.uboard.exception.UboardJsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Lazy
@Component
public class CreateCredentialTaskStagesBuilder implements TaskStagesBuilderInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCredentialTaskStagesBuilder.class);
    private final CustomObjectMapper customObjectMapper;

    public CreateCredentialTaskStagesBuilder(CustomObjectMapper customObjectMapper) {
        this.customObjectMapper = customObjectMapper;
    }

    @Override
    public void configureTaskStages(String payload, TaskBuilder taskBuilder) throws UboardApplicationException {
        LOGGER.info("Setting up stages for creating a credential...");
        String credentialIdentifier = UUID.randomUUID().toString();
        CreateCredentialForm form = this.customObjectMapper.fromJson(payload, CreateCredentialForm.class);

        this.configureCheckingTokenValidityAtProviderStage(taskBuilder, form);
        this.configureRegisteringTokenInSecretsManagerStage(taskBuilder, credentialIdentifier, form);
        this.configurePersistGitCredentialInDatabaseStage(taskBuilder, credentialIdentifier, form);
    }

    private void configureCheckingTokenValidityAtProviderStage(TaskBuilder taskBuilder,
                                                               CreateCredentialForm form) throws UboardJsonProcessingException {
        String checkGitCredentialStageForm = CustomJsonMapper
                .newInstance()
                .addProperty("url", form.url())
                .addProperty("token", form.token())
                .addProperty("type", form.type())
                .build();

        taskBuilder.withStage(new TaskStage(
                "Checking token validity at provider",
                TaskOperationStageEnum.CHECK_GIT_TOKEN_VALIDITY_IN_PROVIDER,
                checkGitCredentialStageForm,
                true
        ));
    }

    private void configureRegisteringTokenInSecretsManagerStage(TaskBuilder taskBuilder,
                                                                String credentialIdentifier,
                                                                CreateCredentialForm form) throws UboardJsonProcessingException {
        String registerCredentialOnVaultForm = CustomJsonMapper.newInstance()
                .addProperty("userIdentifier", taskBuilder.getSessionUser().id())
                .addProperty("credentialIdentifier", credentialIdentifier)
                .addProperty("token", form.token())
                .build();

        taskBuilder.withStage(new TaskStage(
                "Registering token in secrets manager",
                TaskOperationStageEnum.REGISTER_TOKEN_IN_SECRETS_MANAGER,
                registerCredentialOnVaultForm,
                true
        ));
    }

    private void configurePersistGitCredentialInDatabaseStage(TaskBuilder taskBuilder,
                                                              String credentialIdentifier,
                                                              CreateCredentialForm form) throws UboardJsonProcessingException {
        String persistGitCredentialInDatabaseStageForm = CustomJsonMapper.newInstance()
                .addProperty("uuid", credentialIdentifier)
                .addProperty("name", form.name())
                .addProperty("url", form.url())
                .addProperty("type", form.type())
                .addProperty("userIdentifier", taskBuilder.getSessionUser().id())
                .build();

        taskBuilder.withStage(new TaskStage(
                "Persisting credentials in the database",
                TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE,
                persistGitCredentialInDatabaseStageForm
        ));
    }
}
