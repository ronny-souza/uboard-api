package br.com.uboard.messaging;

import br.com.uboard.configuration.RabbitMQConfiguration;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import br.com.uboard.core.model.operations.TaskStageResultForm;
import br.com.uboard.core.repository.TaskStageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class TaskStageResultEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStageResultEventListener.class);

    private final TaskStageRepository taskStageRepository;
    private final RabbitTemplate rabbitTemplate;

    public TaskStageResultEventListener(TaskStageRepository taskStageRepository, RabbitTemplate rabbitTemplate) {
        this.taskStageRepository = taskStageRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfiguration.UBOARD_TASK_STAGE_RESULT_EVENT)
    void execute(TaskStageResultForm form) {
        Optional<TaskStage> taskStageAsOptional = this.taskStageRepository.findByUuid(form.uuid());
        if (taskStageAsOptional.isEmpty()) {
            LOGGER.error("Task stage {} is not found", form.uuid());
        } else {
            TaskStage taskStage = taskStageAsOptional.get();
            taskStage.setFinishedAt(LocalDateTime.now());
            if (form.status().equals(TaskStatusEnum.FAILED)) {
                LOGGER.debug("Updating task stage {} to failed...", form.uuid());
                taskStage.setStatus(TaskStatusEnum.FAILED);
                taskStage.setError(form.error());
                taskStage.setCause(form.errorClassName());
            } else if (form.status().equals(TaskStatusEnum.COMPLETED)) {
                LOGGER.debug("Updating task stage {} to completed...", form.uuid());
                taskStage.setResult(form.response());
                taskStage.setStatus(TaskStatusEnum.COMPLETED);
            }

            List<TaskStage> taskStagesByPriority = this.taskStageRepository.findByPriorityAndTask(
                    taskStage.getPriority(), taskStage.getTask().getUuid()
            );

            boolean isTaskStagesByPriorityFinished = taskStagesByPriority.stream().allMatch(taskStageByPriority ->
                    taskStageByPriority.getStatus().equals(TaskStatusEnum.COMPLETED) ||
                            taskStageByPriority.getStatus().equals(TaskStatusEnum.FAILED)
            );

            if (isTaskStagesByPriorityFinished) {
                this.rabbitTemplate.convertAndSend(RabbitMQConfiguration.UBOARD_TASK_EXECUTION_EVENT, taskStage.getTask().getUuid());
            }
        }
    }
}
