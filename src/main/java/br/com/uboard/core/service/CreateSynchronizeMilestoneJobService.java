package br.com.uboard.core.service;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.enums.SynchronizeMilestoneFrequencyEnum;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.exception.UboardApplicationException;
import br.com.uboard.jobs.MilestoneSynchronizationJob;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Service
public class CreateSynchronizeMilestoneJobService {
    private static final String JOB_KEY_PREFIX = "JOB_SYNCHRONIZE_MILESTONE_";
    private static final String JOB_GROUP = "GROUP_SYNCHRONIZE_MILESTONE";
    private static final String TRIGGER_KEY_PREFIX = "TRIGGER_SYNCHRONIZE_MILESTONE_";

    private final Scheduler scheduler;
    private final CustomObjectMapper customObjectMapper;

    public CreateSynchronizeMilestoneJobService(Scheduler scheduler,
                                                CustomObjectMapper customObjectMapper) {
        this.scheduler = scheduler;
        this.customObjectMapper = customObjectMapper;
    }

    public void configure(Milestone milestone, boolean isUpdating, SessionUserDTO user) throws UboardApplicationException {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_KEY_PREFIX + milestone.getUuid(), JOB_GROUP);
            TriggerKey triggerKey = TriggerKey.triggerKey(TRIGGER_KEY_PREFIX + milestone.getUuid(), JOB_GROUP);
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("organization", milestone.getOrganization().getUuid());
            jobDataMap.put("milestoneProviderId", milestone.getProviderId());
            jobDataMap.put("milestoneTitle", milestone.getTitle());
            jobDataMap.put("isAutoSync", milestone.isAutoSync());
            jobDataMap.put("frequency", milestone.getFrequency().name());
            jobDataMap.put("user", this.customObjectMapper.toJson(user));

            if (milestone.getHours() != null) {
                jobDataMap.put("hours", milestone.getHours());
            }

            if (milestone.getMinutes() != null) {
                jobDataMap.put("minutes", milestone.getMinutes());
            }

            if (milestone.getWeekDay() != null) {
                jobDataMap.put("weekDay", milestone.getWeekDay());
            }

            if (this.scheduler.checkExists(jobKey)) {
                if (!milestone.isAutoSync()) {
                    this.scheduler.deleteJob(jobKey);
                } else if (isUpdating && milestone.isAutoSync() && !milestone.getFrequency().equals(SynchronizeMilestoneFrequencyEnum.NONE)) {
                    CronTrigger cronTrigger = this.configureSimpleTrigger(milestone, triggerKey);
                    this.scheduler.rescheduleJob(triggerKey, cronTrigger);
                }
            } else if (milestone.isAutoSync()) {
                this.scheduleSynchronizeMilestoneJob(milestone, jobKey, jobDataMap, triggerKey);
            }
        } catch (SchedulerException e) {
            throw new UboardApplicationException(e.getMessage());
        }
    }

    private void scheduleSynchronizeMilestoneJob(Milestone milestone, JobKey jobKey, JobDataMap jobDataMap, TriggerKey triggerKey) throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(MilestoneSynchronizationJob.class)
                .withIdentity(jobKey)
                .usingJobData(jobDataMap)
                .storeDurably(false)
                .build();

        CronTrigger cronTrigger = this.configureSimpleTrigger(milestone, triggerKey);

        this.scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    private CronTrigger configureSimpleTrigger(Milestone milestone, TriggerKey triggerKey) {
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(milestone.configureSynchronizationFrequency()))
                .build();
    }
}
