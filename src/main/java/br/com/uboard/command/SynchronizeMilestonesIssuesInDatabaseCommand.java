package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Issue;
import br.com.uboard.core.model.IssueUser;
import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.enums.IssueStateEnum;
import br.com.uboard.core.model.external.GitIssueInterface;
import br.com.uboard.core.model.external.GitUserInterface;
import br.com.uboard.core.model.operations.SynchronizeMilestoneInDatabaseForm;
import br.com.uboard.core.repository.IssueRepository;
import br.com.uboard.core.repository.IssueUserRepository;
import br.com.uboard.core.repository.MilestoneRepository;
import br.com.uboard.core.repository.OrganizationRepository;
import br.com.uboard.core.service.ListGitMilestoneIssuesService;
import br.com.uboard.exception.MilestoneNotFoundException;
import br.com.uboard.exception.UboardApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Lazy
@Component
public class SynchronizeMilestonesIssuesInDatabaseCommand implements TaskStageCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizeMilestonesIssuesInDatabaseCommand.class);
    private final CustomObjectMapper customObjectMapper;
    private final OrganizationRepository organizationRepository;
    private final MilestoneRepository milestoneRepository;
    private final IssueRepository issueRepository;
    private final IssueUserRepository issueUserRepository;

    public SynchronizeMilestonesIssuesInDatabaseCommand(CustomObjectMapper customObjectMapper,
                                                        OrganizationRepository organizationRepository,
                                                        MilestoneRepository milestoneRepository,
                                                        IssueRepository issueRepository,
                                                        IssueUserRepository issueUserRepository,
                                                        ListGitMilestoneIssuesService listGitMilestoneIssuesService) {
        this.customObjectMapper = customObjectMapper;
        this.organizationRepository = organizationRepository;
        this.milestoneRepository = milestoneRepository;
        this.issueRepository = issueRepository;
        this.issueUserRepository = issueUserRepository;
        this.listGitMilestoneIssuesService = listGitMilestoneIssuesService;
    }

    private final ListGitMilestoneIssuesService listGitMilestoneIssuesService;

    @Override
    @Transactional
    public Optional<Object> execute(String payload) throws UboardApplicationException {
        LOGGER.info("Synchronizing milestone issues in database...");
        SynchronizeMilestoneInDatabaseForm form = this.customObjectMapper.fromJson(
                payload, SynchronizeMilestoneInDatabaseForm.class
        );

        Organization organization = this.organizationRepository.getOrganizationByUuidAndUser(
                form.organizationId(), form.user().id()
        );

        Milestone milestone = this.milestoneRepository.findByProviderIdAndOrganizationUuid(
                form.milestoneId(), organization.getUuid()
        ).orElseThrow(() -> new MilestoneNotFoundException(String.format("Milestone %d is not found", form.milestoneId())));

        List<GitIssueInterface<?>> issuesForSynchronize = this.listGitMilestoneIssuesService.listMilestoneIssues(
                organization, milestone.getTitle(), form.user()
        );

        for (GitIssueInterface<?> issueForSynchronize : issuesForSynchronize) {
            Optional<Issue> issueAsOptional = this.issueRepository.findByProviderIdAndMilestoneProviderId(
                    issueForSynchronize.getId(), milestone.getProviderId()
            );

            if (issueAsOptional.isPresent()) {
                Issue issue = issueAsOptional.get();
                issue.setTitle(issueForSynchronize.getTitle());
                issue.setDescription(issueForSynchronize.getDescription());
                issue.setUpdatedAt(issueForSynchronize.getUpdatedAt());
                issue.setUrl(issueForSynchronize.getUrl());
                issue.setLabels(issueForSynchronize.getLabels());
                issue.setState(issueForSynchronize.getState() != null ?
                        IssueStateEnum.valueOf(issueForSynchronize.getState().toUpperCase()) :
                        null
                );
                this.configureIssueAssignees(issueForSynchronize, issue);
                this.configureIssueAuthor(issueForSynchronize, issue);
            } else {
                Issue issue = new Issue(issueForSynchronize, milestone);
                this.configureIssueAssignees(issueForSynchronize, issue);
                this.configureIssueAuthor(issueForSynchronize, issue);

                this.issueRepository.save(issue);
            }
        }

        milestone.getIssues().removeIf(issue -> issuesForSynchronize.stream()
                .noneMatch(issueForSynchronize -> issueForSynchronize.getId().equals(issue.getProviderId())));
        milestone.setSynchronizedAt(LocalDateTime.now());
        return Optional.empty();
    }

    private void configureIssueAuthor(GitIssueInterface<?> issueForSynchronize, Issue issue) {
        if (issueForSynchronize.getAuthor() != null) {
            Optional<IssueUser> authorAsOptional = this.issueUserRepository.findByProviderId(
                    issueForSynchronize.getAuthor().getId()
            );

            if (authorAsOptional.isPresent()) {
                issue.setAuthor(authorAsOptional.get());
            } else {
                issue.setAuthor(issue.getAssignees()
                        .stream()
                        .filter(issueAssignee ->
                                issueAssignee.getProviderId().equals(issueForSynchronize.getAuthor().getId())).findFirst()
                        .orElse(new IssueUser(issueForSynchronize.getAuthor())));
            }
        }
    }

    private void configureIssueAssignees(GitIssueInterface<?> issueForSynchronize, Issue issue) {
        if (issueForSynchronize.getAssignees() != null) {
            for (GitUserInterface assignee : issueForSynchronize.getAssignees()) {
                Optional<IssueUser> assigneeAsOptional = this.issueUserRepository.findByProviderId(assignee.getId());
                if (assigneeAsOptional.isPresent()) {
                    IssueUser assigneeEntity = assigneeAsOptional.get();
                    issue.getAssignees().add(assigneeEntity);
                } else {
                    issue.getAssignees().add(new IssueUser(assignee));
                }
            }
            issue.getAssignees().removeIf(assignee -> issueForSynchronize.getAssignees().stream().noneMatch(issueForSynchronizeAssignee -> issueForSynchronizeAssignee.getId().equals(assignee.getProviderId())));
        }
    }
}
