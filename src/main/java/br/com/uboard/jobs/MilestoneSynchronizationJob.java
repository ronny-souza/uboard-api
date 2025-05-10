package br.com.uboard.jobs;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.enums.SynchronizeMilestoneFrequencyEnum;
import br.com.uboard.core.model.enums.TaskOperationEnum;
import br.com.uboard.core.model.operations.CreateTaskForm;
import br.com.uboard.core.model.operations.OrganizationMilestoneForm;
import br.com.uboard.core.model.operations.SynchronizeMilestoneForm;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.service.CreateTaskService;
import br.com.uboard.exception.CreateTaskException;
import br.com.uboard.exception.UboardJsonProcessingException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MilestoneSynchronizationJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilestoneSynchronizationJob.class);
    private final CreateTaskService createTaskService;
    private final CustomObjectMapper customObjectMapper;

    public MilestoneSynchronizationJob(CreateTaskService createTaskService,
                                       CustomObjectMapper customObjectMapper) {
        this.createTaskService = createTaskService;
        this.customObjectMapper = customObjectMapper;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.debug("Starting milestone auto-synchronization execution...");
        try {
            JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
            SessionUserDTO sessionUserDTO = this.customObjectMapper.fromJson(dataMap.getString("user"), SessionUserDTO.class);

            SynchronizeMilestoneForm form = new SynchronizeMilestoneForm(
                    dataMap.getString("organization"),
                    new OrganizationMilestoneForm(dataMap.getLong("milestoneProviderId"), dataMap.getString("milestoneTitle")),
                    dataMap.getBoolean("isAutoSync"),
                    false,
                    SynchronizeMilestoneFrequencyEnum.valueOf(dataMap.getString("frequency")),
                    dataMap.containsKey("hours") ? dataMap.getInt("hours") : 0,
                    dataMap.containsKey("minutes") ? dataMap.getInt("minutes") : 0,
                    dataMap.containsKey("weekDay") ? dataMap.getInt("weekDay") : 0
            );

            this.createTaskService.create(CreateTaskForm.newInstance(TaskOperationEnum.SYNCHRONIZE_MILESTONE,
                            form,
                            form.milestone().title(),
                            sessionUserDTO
                    )
            );

        } catch (UboardJsonProcessingException | CreateTaskException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
