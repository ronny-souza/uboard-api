package br.com.uboard.messaging;

import br.com.uboard.configuration.RabbitMQConfiguration;
import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import br.com.uboard.core.model.operations.TaskStatusForm;
import br.com.uboard.core.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CompletedTaskEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompletedTaskEventListener.class);

    private final TaskRepository taskRepository;

    public CompletedTaskEventListener(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @RabbitListener(queues = RabbitMQConfiguration.UBOARD_TASK_COMPLETED_EVENT)
    void execute(TaskStatusForm form) {
        LOGGER.debug("Starting task update stage after completion...");
        Optional<Task> taskAsOptional = this.taskRepository.findByUuid(form.uuid());
        if (taskAsOptional.isEmpty()) {
            LOGGER.error("The task was not found at the completion stage");
            return;
        }

        Task task = taskAsOptional.get();
        if (form.status().equals(TaskStatusEnum.COMPLETED)) {
            LOGGER.debug("Task completed successfully. Finalizing processing and updating records...");
            task.setProgress(100);
            task.setStatus(TaskStatusEnum.COMPLETED);
            task.setFinishedAt(LocalDateTime.now());
        } else {
            LOGGER.error("Task not completed. Updating records and finalizing processing...");
            task.setStatus(form.status());
            task.calculateProgress();
        }

        task.getStages().forEach(TaskStage::cleanPayloadSensitiveData);
    }
}
