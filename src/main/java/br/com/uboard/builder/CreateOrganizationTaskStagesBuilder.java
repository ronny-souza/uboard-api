package br.com.uboard.builder;

import br.com.uboard.common.CustomJsonMapper;
import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.operations.CreateOrganizationForm;
import br.com.uboard.exception.UboardApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Lazy
@Component
public class CreateOrganizationTaskStagesBuilder implements TaskStagesBuilderInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateOrganizationTaskStagesBuilder.class);
    private final CustomObjectMapper customObjectMapper;

    public CreateOrganizationTaskStagesBuilder(CustomObjectMapper customObjectMapper) {
        this.customObjectMapper = customObjectMapper;
    }

    @Override
    public void configureTaskStages(String payload, TaskBuilder taskBuilder) throws UboardApplicationException {
        LOGGER.info("Setting up stages for creating an organization...");
        String organizationIdentifier = UUID.randomUUID().toString();
        CreateOrganizationForm form = this.customObjectMapper.fromJson(payload, CreateOrganizationForm.class);

        String createOrganizationFormAsJson = CustomJsonMapper
                .newInstance()
                .addProperty("uuid", organizationIdentifier)
                .addProperty("name", form.name())
                .addProperty("type", form.type())
                .addProperty("scope", form.scope())
                .addProperty("credential", form.credential())
                .addProperty("target", form.target())
                .addProperty("user", taskBuilder.getSessionUser().id())
                .build();

        taskBuilder.withStage(new TaskStage(
                "Persisting organization in database",
                TaskOperationStageEnum.PERSIST_ORGANIZATION_IN_DATABASE,
                createOrganizationFormAsJson,
                true
        ));
    }
}
