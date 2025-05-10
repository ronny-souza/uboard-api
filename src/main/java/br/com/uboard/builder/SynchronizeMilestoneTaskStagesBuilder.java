package br.com.uboard.builder;

import br.com.uboard.common.CustomJsonMapper;
import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.operations.SynchronizeMilestoneForm;
import br.com.uboard.exception.UboardApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SynchronizeMilestoneTaskStagesBuilder implements TaskStagesBuilderInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizeMilestoneTaskStagesBuilder.class);
    private final CustomObjectMapper customObjectMapper;

    public SynchronizeMilestoneTaskStagesBuilder(CustomObjectMapper customObjectMapper) {
        this.customObjectMapper = customObjectMapper;
    }

    @Override
    public void configureTaskStages(String payload, TaskBuilder taskBuilder) throws UboardApplicationException {
        LOGGER.info("Setting up stages for synchronize an organization milestone...");
        SynchronizeMilestoneForm form = this.customObjectMapper.fromJson(payload, SynchronizeMilestoneForm.class);

        String synchronizeMilestoneFormAsJson = CustomJsonMapper
                .newInstance()
                .addProperty("organizationId", form.organization())
                .addProperty("milestoneId", form.milestone().id())
                .addProperty("user", taskBuilder.getSessionUser())
                .addProperty("isAutoSync", form.isAutoSync())
                .addProperty("frequency", form.frequency())
                .addProperty("hours", form.hours())
                .addProperty("minutes", form.minutes())
                .addProperty("weekDay", form.weekDay())
                .build();

        taskBuilder.withStage(new TaskStage(
                "Synchronize milestone in database",
                TaskOperationStageEnum.SYNCHRONIZE_MILESTONE_IN_DATABASE,
                synchronizeMilestoneFormAsJson,
                true
        ));

        String synchronizeMilestoneIssuesFormAsJson = CustomJsonMapper
                .newInstance()
                .addProperty("organizationId", form.organization())
                .addProperty("milestoneId", form.milestone().id())
                .addProperty("user", taskBuilder.getSessionUser())
                .build();


        taskBuilder.withStage(new TaskStage(
                "Synchronize milestone issues in database",
                TaskOperationStageEnum.SYNCHRONIZE_MILESTONE_ISSUES_IN_DATABASE,
                synchronizeMilestoneIssuesFormAsJson,
                true
        ));
    }
}
