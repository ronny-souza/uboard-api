package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.enums.MilestoneStateEnum;
import br.com.uboard.core.model.enums.SynchronizeMilestoneFrequencyEnum;
import br.com.uboard.core.model.external.GitMilestoneInterface;
import br.com.uboard.core.model.operations.SynchronizeMilestoneInDatabaseForm;
import br.com.uboard.core.repository.MilestoneRepository;
import br.com.uboard.core.repository.OrganizationRepository;
import br.com.uboard.core.service.CreateSynchronizeMilestoneJobService;
import br.com.uboard.core.service.GetSingleGitMilestoneService;
import br.com.uboard.exception.UboardApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Lazy
@Component
public class SynchronizeGitMilestoneInDatabaseCommand implements TaskStageCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizeGitMilestoneInDatabaseCommand.class);
    private final CustomObjectMapper customObjectMapper;
    private final OrganizationRepository organizationRepository;
    private final GetSingleGitMilestoneService getSingleGitMilestoneService;
    private final MilestoneRepository milestoneRepository;
    private final CreateSynchronizeMilestoneJobService createSynchronizeMilestoneJobService;

    public SynchronizeGitMilestoneInDatabaseCommand(CustomObjectMapper customObjectMapper,
                                                    OrganizationRepository organizationRepository,
                                                    GetSingleGitMilestoneService getSingleGitMilestoneService,
                                                    MilestoneRepository milestoneRepository,
                                                    CreateSynchronizeMilestoneJobService createSynchronizeMilestoneJobService) {
        this.customObjectMapper = customObjectMapper;
        this.organizationRepository = organizationRepository;
        this.getSingleGitMilestoneService = getSingleGitMilestoneService;
        this.milestoneRepository = milestoneRepository;
        this.createSynchronizeMilestoneJobService = createSynchronizeMilestoneJobService;
    }

    @Override
    @Transactional
    public Optional<Object> execute(String payload) throws UboardApplicationException {
        LOGGER.info("Synchronizing milestone in database...");
        SynchronizeMilestoneInDatabaseForm form = this.customObjectMapper.fromJson(
                payload, SynchronizeMilestoneInDatabaseForm.class
        );

        Organization organization = this.organizationRepository.getOrganizationByUuidAndUser(
                form.organizationId(), form.user().id()
        );

        GitMilestoneInterface milestoneForSynchronization = this.getSingleGitMilestoneService.getSingleMilestone(
                form.milestoneId(), organization, form.user()
        );

        Optional<Milestone> databaseMilestoneAsOptional = this.milestoneRepository.findByProviderIdAndOrganizationUuid(
                form.milestoneId(), organization.getUuid()
        );

        Milestone milestone;
        if (databaseMilestoneAsOptional.isPresent()) {
            milestone = databaseMilestoneAsOptional.get();
            milestone.setTitle(milestoneForSynchronization.getTitle());
            milestone.setState(milestoneForSynchronization.getState() != null ? MilestoneStateEnum.valueOf(milestoneForSynchronization.getState().toUpperCase()) : null);
            milestone.setFinishedAt(milestoneForSynchronization.getDueDate());
            milestone.setSynchronizedAt(LocalDateTime.now());
            milestone.setAutoSync(form.isAutoSync());
            milestone.setFrequency(form.frequency() != null ? form.frequency() : SynchronizeMilestoneFrequencyEnum.NONE);
            milestone.setHours(form.hours());
            milestone.setMinutes(form.minutes());
            milestone.setWeekDay(form.weekDay());
        } else {
            milestone = this.milestoneRepository.save(new Milestone(form, milestoneForSynchronization, organization));
        }

        this.createSynchronizeMilestoneJobService.configure(milestone, false, form.user());
        return Optional.empty();
    }
}
