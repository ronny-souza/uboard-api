package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.TaskStatusEnum;

public record TaskStageResultForm(String uuid,
                                  TaskStatusEnum status,
                                  String response,
                                  String error,
                                  String errorClassName) {

    public TaskStageResultForm(String uuid, TaskStatusEnum status, String response) {
        this(uuid, status, response, null, null);
    }
}
