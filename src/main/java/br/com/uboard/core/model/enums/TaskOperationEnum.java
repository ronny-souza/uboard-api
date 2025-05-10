package br.com.uboard.core.model.enums;

import br.com.uboard.builder.CreateCredentialTaskStagesBuilder;
import br.com.uboard.builder.CreateOrganizationTaskStagesBuilder;
import br.com.uboard.builder.SynchronizeMilestoneTaskStagesBuilder;
import br.com.uboard.builder.TaskStagesBuilderInterface;

public enum TaskOperationEnum {

    CREATE_CREDENTIAL(CreateCredentialTaskStagesBuilder.class),
    CREATE_ORGANIZATION(CreateOrganizationTaskStagesBuilder.class),
    SYNCHRONIZE_MILESTONE(SynchronizeMilestoneTaskStagesBuilder.class),
    IMPORT_MILESTONE(SynchronizeMilestoneTaskStagesBuilder.class);;

    private final Class<? extends TaskStagesBuilderInterface> taskStagesBuilder;

    TaskOperationEnum(Class<? extends TaskStagesBuilderInterface> taskStagesBuilder) {
        this.taskStagesBuilder = taskStagesBuilder;
    }

    public Class<? extends TaskStagesBuilderInterface> getTaskStagesBuilder() {
        return taskStagesBuilder;
    }
}
