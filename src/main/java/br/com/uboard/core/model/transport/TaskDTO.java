package br.com.uboard.core.model.transport;

import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.enums.TaskOperationEnum;
import br.com.uboard.core.model.enums.TaskStatusEnum;

import java.time.LocalDateTime;

public record TaskDTO(String uuid,
                      TaskOperationEnum operation,
                      TaskStatusEnum status,
                      String detail,
                      Integer progress,
                      LocalDateTime createdAt,
                      LocalDateTime finishedAt) {

    public TaskDTO(Task task) {
        this(
                task.getUuid(),
                task.getOperation(),
                task.getStatus(),
                task.getDetail(),
                task.getProgress(),
                task.getCreatedAt(),
                task.getFinishedAt()
        );
    }
}
