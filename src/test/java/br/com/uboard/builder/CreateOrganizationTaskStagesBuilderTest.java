package br.com.uboard.builder;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.enums.ScopeEnum;
import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.operations.CreateOrganizationForm;
import br.com.uboard.core.model.operations.OrganizationTargetForm;
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
class CreateOrganizationTaskStagesBuilderTest {

    @InjectMocks
    private CreateOrganizationTaskStagesBuilder createOrganizationTaskStagesBuilder;

    @Mock
    private CustomObjectMapper customObjectMapper;

    @Test
    @DisplayName("Should configure necessary stages to create an organization")
    void shouldConfigureNecessaryStagesToCreateOrganization() throws UboardApplicationException {
        TaskBuilder taskBuilderAsMock = mock(TaskBuilder.class);
        CreateOrganizationForm formAsMock = mock(CreateOrganizationForm.class);
        SessionUserDTO sessionUserDTOAsMock = mock(SessionUserDTO.class);
        ArgumentCaptor<TaskStage> taskStageArgumentCaptor = forClass(TaskStage.class);

        when(this.customObjectMapper.fromJson(anyString(), eq(CreateOrganizationForm.class))).thenReturn(formAsMock);
        when(formAsMock.name()).thenReturn("name");
        when(formAsMock.type()).thenReturn(ProviderEnum.GITLAB);
        when(formAsMock.scope()).thenReturn(ScopeEnum.GROUP);
        when(formAsMock.credential()).thenReturn("credential");
        when(formAsMock.target()).thenReturn(mock(OrganizationTargetForm.class));
        when(taskBuilderAsMock.getSessionUser()).thenReturn(sessionUserDTOAsMock);
        when(sessionUserDTOAsMock.id()).thenReturn("id");

        this.createOrganizationTaskStagesBuilder.configureTaskStages("{}", taskBuilderAsMock);

        verify(taskBuilderAsMock, times(1)).withStage(taskStageArgumentCaptor.capture());

        TaskStage persistOrganizationInDatabaseStage = taskStageArgumentCaptor.getAllValues()
                .stream()
                .filter(taskStage -> taskStage.getStage().equals(TaskOperationStageEnum.PERSIST_ORGANIZATION_IN_DATABASE))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Task stage %s is not found on stages", TaskOperationStageEnum.CHECK_GIT_TOKEN_VALIDITY_IN_PROVIDER)));

        assertNotNull(persistOrganizationInDatabaseStage);
        assertNotNull(persistOrganizationInDatabaseStage.getPayload());
        assertEquals("Persisting organization in database", persistOrganizationInDatabaseStage.getDescription());
        assertTrue(persistOrganizationInDatabaseStage.isSensitivePayload());
    }
}