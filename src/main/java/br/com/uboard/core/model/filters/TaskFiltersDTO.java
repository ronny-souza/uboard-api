package br.com.uboard.core.model.filters;

import br.com.uboard.core.model.enums.TaskStatusEnum;

public record TaskFiltersDTO(TaskStatusEnum status) {
}
