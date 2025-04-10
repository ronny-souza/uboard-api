package br.com.uboard.messaging;

import br.com.uboard.command.CreateTokenInSecretManagerCommand;
import br.com.uboard.command.PersistGitCredentialOnDatabaseCommand;
import br.com.uboard.command.TaskStageCommand;
import br.com.uboard.command.ValidateGitTokenOnProviderCommand;
import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.configuration.RabbitMQConfiguration;
import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import br.com.uboard.core.model.operations.TaskStageResultForm;
import br.com.uboard.core.model.transport.TaskStageDTO;
import br.com.uboard.exception.UboardJsonProcessingException;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RunTaskStageEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunTaskStageEventListener.class);

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationContext applicationContext;
    private final CustomObjectMapper customObjectMapper;

    public RunTaskStageEventListener(RabbitTemplate rabbitTemplate,
                                     ApplicationContext applicationContext,
                                     CustomObjectMapper customObjectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.applicationContext = applicationContext;
        this.customObjectMapper = customObjectMapper;
    }

    @RabbitListener(queues = RabbitMQConfiguration.UBOARD_RUN_TASK_STAGE)
    void execute(TaskStageDTO taskStageDTO) {
        LOGGER.info("Starting processing of a task stage...");
        try {
            TaskStageCommand command = this.getCommand(taskStageDTO.stage());
            Optional<Object> responseAsOptional = command.execute(taskStageDTO.payload());

            String responseAsString = "";
            if (responseAsOptional.isPresent()) {
                responseAsString = this.convertResponseAsString(responseAsOptional.get());
            }

            this.rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.UBOARD_TASK_STAGE_RESULT_EVENT,
                    new TaskStageResultForm(
                            taskStageDTO.uuid(),
                            TaskStatusEnum.COMPLETED,
                            responseAsString
                    )
            );
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            String error = e.getMessage();
            String errorClassName = e.getLocalizedMessage();

            Throwable cause = e.getCause();
            if (cause != null) {
                error = cause.getMessage();
                errorClassName = cause.getClass().getSimpleName();
            }

            this.rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.UBOARD_TASK_STAGE_RESULT_EVENT,
                    new TaskStageResultForm(
                            taskStageDTO.uuid(),
                            TaskStatusEnum.COMPLETED,
                            "FAILED",
                            error,
                            errorClassName
                    )
            );
        }
    }

    private String convertResponseAsString(Object response) {
        switch (response) {
            case String responseAsString -> {
                return responseAsString;
            }
            case JsonObject responseAsJsonObject -> {
                return responseAsJsonObject.toString();
            }
            default -> {
                try {
                    return this.customObjectMapper.toJson(response);
                } catch (UboardJsonProcessingException e) {
                    return "";
                }
            }
        }
    }

    private TaskStageCommand getCommand(TaskOperationStageEnum operation) {
        Class<? extends TaskStageCommand> command = switch (operation) {
            case CHECK_GIT_TOKEN_VALIDITY_IN_PROVIDER -> ValidateGitTokenOnProviderCommand.class;
            case REGISTER_TOKEN_IN_SECRETS_MANAGER -> CreateTokenInSecretManagerCommand.class;
            case PERSIST_GIT_CREDENTIAL_IN_DATABASE -> PersistGitCredentialOnDatabaseCommand.class;
        };

        return this.applicationContext.getBean(command);
    }
}
