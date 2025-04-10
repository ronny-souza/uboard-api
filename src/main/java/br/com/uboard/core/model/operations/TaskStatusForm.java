package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.TaskStatusEnum;

public record TaskStatusForm(String uuid, TaskStatusEnum status) {
}
