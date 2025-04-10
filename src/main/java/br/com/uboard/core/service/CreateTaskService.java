package br.com.uboard.core.service;

import br.com.uboard.builder.TaskBuilder;
import br.com.uboard.builder.TaskStagesBuilderInterface;
import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.configuration.RabbitMQConfiguration;
import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.operations.CreateTaskForm;
import br.com.uboard.core.model.transport.TaskDTO;
import br.com.uboard.core.repository.TaskRepository;
import br.com.uboard.exception.CreateTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CreateTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTaskService.class);
    private final CustomObjectMapper customObjectMapper;
    private final TaskRepository taskRepository;
    private final ApplicationContext applicationContext;
    private final RabbitTemplate rabbitTemplate;

    public CreateTaskService(CustomObjectMapper customObjectMapper,
                             TaskRepository taskRepository,
                             ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
        this.customObjectMapper = customObjectMapper;
        this.taskRepository = taskRepository;
        this.applicationContext = applicationContext;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public TaskDTO create(CreateTaskForm form) throws CreateTaskException {
        try {
            LOGGER.info("Starting task logging {}...", form.getOperation().name());
            Task task = new Task(form);

            TaskBuilder taskBuilder = TaskBuilder.startWithTaskAndUser(task, form.getSessionUser());
            TaskStagesBuilderInterface taskStagesBuilder = this.applicationContext.getBean(
                    task.getOperation().getTaskStagesBuilder()
            );

            taskStagesBuilder.configureTaskStages(this.customObjectMapper.toJson(form.getPayload()), taskBuilder);
            List<TaskStage> taskStages = taskBuilder.getStages();
            LOGGER.debug("Setting stages for task {} resulted in {} stages...", task.getOperation().name(), taskStages.size());
            task.setStages(taskStages);

            Task taskWithPersistenceContext = this.taskRepository.saveAndFlush(task);

            LOGGER.debug("Emitting task creation event...");
            this.rabbitTemplate.convertAndSend(RabbitMQConfiguration.UBOARD_TASK_EXECUTION_EVENT, taskWithPersistenceContext.getUuid());
            return new TaskDTO(taskWithPersistenceContext);
        } catch (Exception e) {
            throw new CreateTaskException(e.getMessage());
        }
    }
}