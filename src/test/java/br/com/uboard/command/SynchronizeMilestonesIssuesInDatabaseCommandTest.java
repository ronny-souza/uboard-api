package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Issue;
import br.com.uboard.core.model.IssueUser;
import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.external.GitIssueInterface;
import br.com.uboard.core.model.external.gitlab.GitlabIssueDTO;
import br.com.uboard.core.model.external.gitlab.GitlabUserDTO;
import br.com.uboard.core.model.operations.SynchronizeMilestoneInDatabaseForm;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.IssueRepository;
import br.com.uboard.core.repository.IssueUserRepository;
import br.com.uboard.core.repository.MilestoneRepository;
import br.com.uboard.core.repository.OrganizationRepository;
import br.com.uboard.core.service.ListGitMilestoneIssuesService;
import br.com.uboard.exception.UboardApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SynchronizeMilestonesIssuesInDatabaseCommandTest {

    @InjectMocks
    private SynchronizeMilestonesIssuesInDatabaseCommand synchronizeMilestonesIssuesInDatabaseCommand;

    @Mock
    private CustomObjectMapper customObjectMapper;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private IssueUserRepository issueUserRepository;

    @Mock
    private ListGitMilestoneIssuesService listGitMilestoneIssuesService;

    @Test
    @DisplayName("Should synchronize milestone issues in database when issue already exists")
    void shouldSynchronizeMilestoneIssuesInDatabaseWhenIssueAlreadyExists() throws UboardApplicationException {
        SynchronizeMilestoneInDatabaseForm formAsMock = mock(SynchronizeMilestoneInDatabaseForm.class);
        SessionUserDTO sessionUserDTOAsMock = mock(SessionUserDTO.class);

        Organization organizationAsMock = mock(Organization.class);
        Milestone milestoneAsMock = mock(Milestone.class);
        GitlabIssueDTO gitlabIssueDTOAsMock = mock(GitlabIssueDTO.class);
        GitlabUserDTO assigneeDTOAsMock = mock(GitlabUserDTO.class);
        GitlabUserDTO authorDTOAsMock = mock(GitlabUserDTO.class);
        List<GitIssueInterface<?>> issues = List.of(gitlabIssueDTOAsMock);
        List<GitlabUserDTO> assignees = List.of(assigneeDTOAsMock);
        Issue issueAsMock = mock(Issue.class);
        IssueUser issueUserAsMock = mock(IssueUser.class);

        when(this.customObjectMapper.fromJson(anyString(), eq(SynchronizeMilestoneInDatabaseForm.class))).thenReturn(formAsMock);
        when(formAsMock.organizationId()).thenReturn("organizationId");
        when(formAsMock.user()).thenReturn(sessionUserDTOAsMock);
        when(sessionUserDTOAsMock.id()).thenReturn("id");
        when(this.organizationRepository.getOrganizationByUuidAndUser(anyString(), anyString())).thenReturn(organizationAsMock);
        when(organizationAsMock.getUuid()).thenReturn("organizationId");
        when(formAsMock.milestoneId()).thenReturn(1L);
        when(this.milestoneRepository.findByProviderIdAndOrganizationUuid(anyLong(), anyString())).thenReturn(Optional.of(milestoneAsMock));
        when(milestoneAsMock.getTitle()).thenReturn("title");
        when(this.listGitMilestoneIssuesService.listMilestoneIssues(any(Organization.class), anyString(), any(SessionUserDTO.class))).thenReturn(issues);
        when(gitlabIssueDTOAsMock.getId()).thenReturn(1L);
        when(milestoneAsMock.getProviderId()).thenReturn(1L);
        when(this.issueRepository.findByProviderIdAndMilestoneProviderId(anyLong(), anyLong())).thenReturn(Optional.of(issueAsMock));
        when(gitlabIssueDTOAsMock.getTitle()).thenReturn("title");
        when(gitlabIssueDTOAsMock.getDescription()).thenReturn("description");
        when(gitlabIssueDTOAsMock.getUrl()).thenReturn("url");
        when(gitlabIssueDTOAsMock.getLabels()).thenReturn(List.of("1"));
        when(gitlabIssueDTOAsMock.getState()).thenReturn("opened");
        when(gitlabIssueDTOAsMock.getAssignees()).thenReturn(assignees);
        when(assigneeDTOAsMock.getId()).thenReturn(1L);
        when(this.issueUserRepository.findByProviderId(anyLong())).thenReturn(Optional.of(issueUserAsMock));
        when(gitlabIssueDTOAsMock.getAuthor()).thenReturn(authorDTOAsMock);
        when(authorDTOAsMock.getId()).thenReturn(1L);
        when(this.issueUserRepository.findByProviderId(anyLong())).thenReturn(Optional.of(issueUserAsMock));

        assertDoesNotThrow(() -> this.synchronizeMilestonesIssuesInDatabaseCommand.execute("{}"));
    }

    @Test
    @DisplayName("Should synchronize milestone issues in database when issue does not exists")
    void shouldSynchronizeMilestoneIssuesInDatabaseWhenIssueDoesNotExists() throws UboardApplicationException {
        SynchronizeMilestoneInDatabaseForm formAsMock = mock(SynchronizeMilestoneInDatabaseForm.class);
        SessionUserDTO sessionUserDTOAsMock = mock(SessionUserDTO.class);

        Organization organizationAsMock = mock(Organization.class);
        Milestone milestoneAsMock = mock(Milestone.class);
        GitlabIssueDTO gitlabIssueDTOAsMock = mock(GitlabIssueDTO.class);
        GitlabUserDTO assigneeDTOAsMock = mock(GitlabUserDTO.class);
        GitlabUserDTO authorDTOAsMock = mock(GitlabUserDTO.class);
        List<GitIssueInterface<?>> issues = List.of(gitlabIssueDTOAsMock);
        List<GitlabUserDTO> assignees = List.of(assigneeDTOAsMock);

        when(this.customObjectMapper.fromJson(anyString(), eq(SynchronizeMilestoneInDatabaseForm.class))).thenReturn(formAsMock);
        when(formAsMock.organizationId()).thenReturn("organizationId");
        when(formAsMock.user()).thenReturn(sessionUserDTOAsMock);
        when(sessionUserDTOAsMock.id()).thenReturn("id");
        when(this.organizationRepository.getOrganizationByUuidAndUser(anyString(), anyString())).thenReturn(organizationAsMock);
        when(organizationAsMock.getUuid()).thenReturn("organizationId");
        when(formAsMock.milestoneId()).thenReturn(1L);
        when(this.milestoneRepository.findByProviderIdAndOrganizationUuid(anyLong(), anyString())).thenReturn(Optional.of(milestoneAsMock));
        when(milestoneAsMock.getTitle()).thenReturn("title");
        when(this.listGitMilestoneIssuesService.listMilestoneIssues(any(Organization.class), anyString(), any(SessionUserDTO.class))).thenReturn(issues);
        when(gitlabIssueDTOAsMock.getId()).thenReturn(1L);
        when(milestoneAsMock.getProviderId()).thenReturn(1L);
        when(this.issueRepository.findByProviderIdAndMilestoneProviderId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(gitlabIssueDTOAsMock.getTitle()).thenReturn("title");
        when(gitlabIssueDTOAsMock.getDescription()).thenReturn("description");
        when(gitlabIssueDTOAsMock.getUrl()).thenReturn("url");
        when(gitlabIssueDTOAsMock.getLabels()).thenReturn(List.of("1"));
        when(gitlabIssueDTOAsMock.getState()).thenReturn("opened");
        when(gitlabIssueDTOAsMock.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(gitlabIssueDTOAsMock.getUpdatedAt()).thenReturn(LocalDateTime.now());
        when(gitlabIssueDTOAsMock.getAssignees()).thenReturn(assignees);
        when(assigneeDTOAsMock.getId()).thenReturn(1L);
        when(this.issueUserRepository.findByProviderId(anyLong())).thenReturn(Optional.empty());
        when(assigneeDTOAsMock.getUsername()).thenReturn("username");
        when(assigneeDTOAsMock.getName()).thenReturn("name");
        when(assigneeDTOAsMock.getAvatarUrl()).thenReturn("avatarUrl");
        when(gitlabIssueDTOAsMock.getAuthor()).thenReturn(authorDTOAsMock);
        when(authorDTOAsMock.getId()).thenReturn(1L);
        when(this.issueUserRepository.findByProviderId(anyLong())).thenReturn(Optional.empty());

        this.synchronizeMilestonesIssuesInDatabaseCommand.execute("{}");

        verify(this.issueRepository).save(any(Issue.class));
    }
}