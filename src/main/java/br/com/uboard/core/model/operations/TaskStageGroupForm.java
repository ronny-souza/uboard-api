package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import org.springframework.util.CollectionUtils;

import java.util.List;

public record TaskStageGroupForm(Task task, Integer priority, List<TaskStage> stages) {

    public boolean isAllStagesSuccessfullyExecuted() {
        return this.stages.stream()
                .allMatch(taskStage -> taskStage.getStatus().equals(TaskStatusEnum.COMPLETED));
    }

    public boolean hasStages() {
        return !CollectionUtils.isEmpty(stages);
    }
}
