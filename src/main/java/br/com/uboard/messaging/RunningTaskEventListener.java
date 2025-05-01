package br.com.uboard.messaging;

import br.com.uboard.configuration.RabbitMQConfiguration;
import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import br.com.uboard.core.model.operations.TaskStageGroupForm;
import br.com.uboard.core.model.operations.TaskStatusForm;
import br.com.uboard.core.model.transport.TaskStageDTO;
import br.com.uboard.core.repository.TaskRepository;
import br.com.uboard.core.repository.TaskStageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RunningTaskEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunningTaskEventListener.class);
    private final TaskRepository taskRepository;
    private final TaskStageRepository taskStageRepository;
    private final RabbitTemplate rabbitTemplate;

    public RunningTaskEventListener(TaskRepository taskRepository,
                                    TaskStageRepository taskStageRepository,
                                    RabbitTemplate rabbitTemplate) {
        this.taskRepository = taskRepository;
        this.taskStageRepository = taskStageRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfiguration.UBOARD_TASK_EXECUTION_EVENT)
    public void execute(String taskIdentifier) {
        if (!StringUtils.hasText(taskIdentifier)) {
            LOGGER.error("The task identifier could not be found during its execution");
            return;
        }

        Optional<Task> taskAsOptional = this.taskRepository.findByUuid(taskIdentifier);
        if (taskAsOptional.isEmpty()) {
            LOGGER.error("The task could not be found during its execution");
            return;
        }

        Task task = taskAsOptional.get();
        Long failedStageCount = this.taskStageRepository.countByStatusAndTask(TaskStatusEnum.FAILED, task.getUuid());
        if (failedStageCount > 0) {
            this.rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.UBOARD_TASK_COMPLETED_EVENT,
                    new TaskStatusForm(task.getUuid(), TaskStatusEnum.FAILED)
            );
            return;
        }

        Boolean allTaskStagesAreCompleted = this.taskStageRepository.allStagesOfTaskAreInCertainState(TaskStatusEnum.COMPLETED, task.getUuid());
        if (Boolean.TRUE.equals(allTaskStagesAreCompleted)) {
            this.rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.UBOARD_TASK_COMPLETED_EVENT,
                    new TaskStatusForm(task.getUuid(), TaskStatusEnum.COMPLETED)
            );
        } else {
            List<TaskStageDTO> createdStagesToRun = getCreatedStagesToRun(task);
            createdStagesToRun.forEach(taskStageDTO -> this.rabbitTemplate
                    .convertAndSend(RabbitMQConfiguration.UBOARD_RUN_TASK_STAGE, taskStageDTO)
            );
            task.calculateProgress();
        }

    }

    private List<TaskStageDTO> getCreatedStagesToRun(Task task) {
        Map<Integer, List<TaskStage>> taskStagesGroupedByPriority = this.taskStageRepository.findByTask(task)
                .stream().collect(Collectors.groupingBy(TaskStage::getPriority));

        List<TaskStageGroupForm> taskStagesGroups = taskStagesGroupedByPriority.entrySet().stream().map(taskStagesGroup ->
                        new TaskStageGroupForm(task, taskStagesGroup.getKey(), taskStagesGroup.getValue()))
                .sorted(Comparator.comparingInt(TaskStageGroupForm::priority)).toList();

        for (TaskStageGroupForm taskStagesGroup : taskStagesGroups) {
            if (!taskStagesGroup.hasStages() || taskStagesGroup.isAllStagesSuccessfullyExecuted()) {
                continue;
            }

            return taskStagesGroup.stages().stream().map(TaskStageDTO::new).toList();
        }
        return Collections.emptyList();
    }
}
