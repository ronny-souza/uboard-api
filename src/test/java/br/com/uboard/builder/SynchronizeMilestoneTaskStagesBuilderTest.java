package br.com.uboard.builder;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.SynchronizeMilestoneFrequencyEnum;
import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.operations.OrganizationMilestoneForm;
import br.com.uboard.core.model.operations.SynchronizeMilestoneForm;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.exception.UboardApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SynchronizeMilestoneTaskStagesBuilderTest {

    @InjectMocks
    private SynchronizeMilestoneTaskStagesBuilder synchronizeMilestoneTaskStagesBuilder;

    @Mock
    private CustomObjectMapper customObjectMapper;

    @Test
    @DisplayName("Should configure necessary stages to synchronize a milestone")
    void shouldConfigureNecessaryStagesToSynchronizeMilestone() throws UboardApplicationException {
        TaskBuilder taskBuilderAsMock = mock(TaskBuilder.class);
        SynchronizeMilestoneForm formAsMock = mock(SynchronizeMilestoneForm.class);
        OrganizationMilestoneForm organizationMilestoneFormAsMock = mock(OrganizationMilestoneForm.class);
        SessionUserDTO sessionUserDTOAsMock = mock(SessionUserDTO.class);
        ArgumentCaptor<TaskStage> taskStageArgumentCaptor = forClass(TaskStage.class);

        when(this.customObjectMapper.fromJson(anyString(), eq(SynchronizeMilestoneForm.class))).thenReturn(formAsMock);
        when(formAsMock.organization()).thenReturn("organization");
        when(formAsMock.milestone()).thenReturn(organizationMilestoneFormAsMock);
        when(organizationMilestoneFormAsMock.id()).thenReturn(1L);
        when(taskBuilderAsMock.getSessionUser()).thenReturn(sessionUserDTOAsMock);
        when(formAsMock.isAutoSync()).thenReturn(true);
        when(formAsMock.frequency()).thenReturn(SynchronizeMilestoneFrequencyEnum.ONCE_A_WEEK);
        when(formAsMock.hours()).thenReturn(1);
        when(formAsMock.minutes()).thenReturn(1);
        when(formAsMock.weekDay()).thenReturn(1);

        this.synchronizeMilestoneTaskStagesBuilder.configureTaskStages("{}", taskBuilderAsMock);

        verify(taskBuilderAsMock, times(2)).withStage(taskStageArgumentCaptor.capture());

        TaskStage synchronizeMilestoneInDatabaseStage = taskStageArgumentCaptor.getAllValues()
                .stream()
                .filter(taskStage -> taskStage.getStage().equals(TaskOperationStageEnum.SYNCHRONIZE_MILESTONE_IN_DATABASE))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Task stage %s is not found on stages", TaskOperationStageEnum.SYNCHRONIZE_MILESTONE_IN_DATABASE)));

        assertNotNull(synchronizeMilestoneInDatabaseStage);
        assertNotNull(synchronizeMilestoneInDatabaseStage.getPayload());
        assertEquals("Synchronize milestone in database", synchronizeMilestoneInDatabaseStage.getDescription());
        assertTrue(synchronizeMilestoneInDatabaseStage.isSensitivePayload());

        TaskStage synchronizeMilestoneIssuesInDatabaseStage = taskStageArgumentCaptor.getAllValues()
                .stream()
                .filter(taskStage -> taskStage.getStage().equals(TaskOperationStageEnum.SYNCHRONIZE_MILESTONE_ISSUES_IN_DATABASE))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Task stage %s is not found on stages", TaskOperationStageEnum.SYNCHRONIZE_MILESTONE_ISSUES_IN_DATABASE)));

        assertNotNull(synchronizeMilestoneIssuesInDatabaseStage);
        assertNotNull(synchronizeMilestoneIssuesInDatabaseStage.getPayload());
        assertEquals("Synchronize milestone issues in database", synchronizeMilestoneIssuesInDatabaseStage.getDescription());
        assertTrue(synchronizeMilestoneIssuesInDatabaseStage.isSensitivePayload());
    }
}