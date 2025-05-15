package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.enums.MilestoneStateEnum;
import br.com.uboard.core.model.enums.SynchronizeMilestoneFrequencyEnum;
import br.com.uboard.core.model.external.gitlab.GitlabMilestoneDTO;
import br.com.uboard.core.model.operations.SynchronizeMilestoneInDatabaseForm;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.MilestoneRepository;
import br.com.uboard.core.repository.OrganizationRepository;
import br.com.uboard.core.service.CreateSynchronizeMilestoneJobService;
import br.com.uboard.core.service.GetSingleGitMilestoneService;
import br.com.uboard.exception.UboardApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SynchronizeGitMilestoneInDatabaseCommandTest {

    @InjectMocks
    private SynchronizeGitMilestoneInDatabaseCommand synchronizeGitMilestoneInDatabaseCommand;

    @Mock
    private CustomObjectMapper customObjectMapper;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private GetSingleGitMilestoneService getSingleGitMilestoneService;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private CreateSynchronizeMilestoneJobService createSynchronizeMilestoneJobService;

    @Test
    @DisplayName("Should synchronize the organization milestone in database when already exists locally")
    void shouldSynchronizeOrganizationMilestoneInDatabaseWhenAlreadyExistsLocally() throws UboardApplicationException {
        SynchronizeMilestoneInDatabaseForm formAsMock = mock(SynchronizeMilestoneInDatabaseForm.class);
        SessionUserDTO sessionUserDTOAsMock = mock(SessionUserDTO.class);
        Organization organizationAsMock = mock(Organization.class);
        GitlabMilestoneDTO gitMilestoneAsMock = mock(GitlabMilestoneDTO.class);
        Milestone milestoneAsMock = mock(Milestone.class);

        when(this.customObjectMapper.fromJson(anyString(), eq(SynchronizeMilestoneInDatabaseForm.class))).thenReturn(formAsMock);
        when(formAsMock.organizationId()).thenReturn("organizationId");
        when(formAsMock.user()).thenReturn(sessionUserDTOAsMock);
        when(sessionUserDTOAsMock.id()).thenReturn("id");
        when(this.organizationRepository.getOrganizationByUuidAndUser(anyString(), anyString())).thenReturn(organizationAsMock);
        when(formAsMock.milestoneId()).thenReturn(1L);
        when(this.getSingleGitMilestoneService.getSingleMilestone(anyLong(), any(Organization.class), any(SessionUserDTO.class))).thenReturn(gitMilestoneAsMock);
        when(organizationAsMock.getUuid()).thenReturn("uuid");
        when(this.milestoneRepository.findByProviderIdAndOrganizationUuid(anyLong(), anyString())).thenReturn(Optional.of(milestoneAsMock));
        when(gitMilestoneAsMock.getTitle()).thenReturn("title");
        when(gitMilestoneAsMock.getState()).thenReturn("active");
        when(gitMilestoneAsMock.getDueDate()).thenReturn(LocalDate.now());
        when(formAsMock.isAutoSync()).thenReturn(true);
        when(formAsMock.frequency()).thenReturn(SynchronizeMilestoneFrequencyEnum.ONCE_A_WEEK);
        when(formAsMock.hours()).thenReturn(1);
        when(formAsMock.minutes()).thenReturn(1);
        when(formAsMock.weekDay()).thenReturn(2);
        this.synchronizeGitMilestoneInDatabaseCommand.execute("{}");

        verify(milestoneAsMock).setTitle(anyString());
        verify(milestoneAsMock).setState(any(MilestoneStateEnum.class));
        verify(milestoneAsMock).setFinishedAt(any(LocalDate.class));
        verify(milestoneAsMock).setSynchronizedAt(any(LocalDateTime.class));
        verify(milestoneAsMock).setAutoSync(anyBoolean());
        verify(milestoneAsMock).setFrequency(any(SynchronizeMilestoneFrequencyEnum.class));
        verify(milestoneAsMock).setHours(anyInt());
        verify(milestoneAsMock).setMinutes(anyInt());
        verify(milestoneAsMock).setWeekDay(anyInt());
    }

    @Test
    @DisplayName("Should synchronize the organization milestone in database when not exists")
    void shouldSynchronizeOrganizationMilestoneInDatabaseWhenNotExists() throws UboardApplicationException {
        SynchronizeMilestoneInDatabaseForm formAsMock = mock(SynchronizeMilestoneInDatabaseForm.class);
        SessionUserDTO sessionUserDTOAsMock = mock(SessionUserDTO.class);
        Organization organizationAsMock = mock(Organization.class);
        GitlabMilestoneDTO gitMilestoneAsMock = mock(GitlabMilestoneDTO.class);

        when(this.customObjectMapper.fromJson(anyString(), eq(SynchronizeMilestoneInDatabaseForm.class))).thenReturn(formAsMock);
        when(formAsMock.organizationId()).thenReturn("organizationId");
        when(formAsMock.user()).thenReturn(sessionUserDTOAsMock);
        when(sessionUserDTOAsMock.id()).thenReturn("id");
        when(this.organizationRepository.getOrganizationByUuidAndUser(anyString(), anyString())).thenReturn(organizationAsMock);
        when(formAsMock.milestoneId()).thenReturn(1L);
        when(this.getSingleGitMilestoneService.getSingleMilestone(anyLong(), any(Organization.class), any(SessionUserDTO.class))).thenReturn(gitMilestoneAsMock);
        when(organizationAsMock.getUuid()).thenReturn("uuid");
        when(this.milestoneRepository.findByProviderIdAndOrganizationUuid(anyLong(), anyString())).thenReturn(Optional.empty());
        when(gitMilestoneAsMock.getId()).thenReturn(1L);
        when(gitMilestoneAsMock.getStartDate()).thenReturn(LocalDate.now());
        when(gitMilestoneAsMock.getTitle()).thenReturn("title");
        when(gitMilestoneAsMock.getState()).thenReturn("active");
        when(gitMilestoneAsMock.getDueDate()).thenReturn(LocalDate.now());
        when(formAsMock.isAutoSync()).thenReturn(true);
        when(formAsMock.frequency()).thenReturn(SynchronizeMilestoneFrequencyEnum.ONCE_A_WEEK);
        when(formAsMock.hours()).thenReturn(1);
        when(formAsMock.minutes()).thenReturn(1);
        when(formAsMock.weekDay()).thenReturn(2);
        this.synchronizeGitMilestoneInDatabaseCommand.execute("{}");

        verify(this.milestoneRepository).save(any(Milestone.class));
    }
}