package br.com.uboard.core.model.transport;

import br.com.uboard.core.model.enums.TaskStatusEnum;

public interface TaskProgressDTO {
    TaskStatusEnum getStatus();

    Integer getProgress();
}
