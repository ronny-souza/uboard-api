package br.com.uboard.core.model.transport;

import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.TaskOperationStageEnum;

public record TaskStageDTO(String uuid,
                           TaskOperationStageEnum stage,
                           String payload) {

    public TaskStageDTO(TaskStage taskStage) {
        this(
                taskStage.getUuid(),
                taskStage.getStage(),
                taskStage.getPayloadWithPayloadInjections()
        );
    }
}
