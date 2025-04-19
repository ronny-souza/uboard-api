package br.com.uboard.builder;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.GitProviderEnum;
import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.operations.CreateCredentialForm;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.exception.UboardApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCredentialTaskStagesBuilderTest {

    @InjectMocks
    private CreateCredentialTaskStagesBuilder createCredentialTaskStagesBuilder;

    @Mock
    private CustomObjectMapper customObjectMapper;

    @Test
    @DisplayName("Should configure necessary stages to create a credential")
    void shouldConfigureNecessaryStagesToCreateCredential() throws UboardApplicationException {
        TaskBuilder taskBuilderAsMock = mock(TaskBuilder.class);
        CreateCredentialForm formAsMock = mock(CreateCredentialForm.class);
        SessionUserDTO sessionUserDTOAsMock = mock(SessionUserDTO.class);
        ArgumentCaptor<TaskStage> taskStageArgumentCaptor = forClass(TaskStage.class);

        when(this.customObjectMapper.fromJson(anyString(), Mockito.eq(CreateCredentialForm.class))).thenReturn(formAsMock);
        when(formAsMock.url()).thenReturn("url");
        when(formAsMock.token()).thenReturn("token");
        when(formAsMock.type()).thenReturn(GitProviderEnum.GITLAB);
        when(taskBuilderAsMock.getSessionUser()).thenReturn(sessionUserDTOAsMock);
        when(sessionUserDTOAsMock.id()).thenReturn("userIdentifier");
        when(formAsMock.name()).thenReturn("name");

        this.createCredentialTaskStagesBuilder.configureTaskStages("{}", taskBuilderAsMock);

        verify(taskBuilderAsMock, times(3)).withStage(taskStageArgumentCaptor.capture());

        TaskStage checkGitTokenValidityInProviderStage = taskStageArgumentCaptor.getAllValues()
                .stream()
                .filter(taskStage -> taskStage.getStage().equals(TaskOperationStageEnum.CHECK_GIT_TOKEN_VALIDITY_IN_PROVIDER))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Task stage %s is not found on stages", TaskOperationStageEnum.CHECK_GIT_TOKEN_VALIDITY_IN_PROVIDER)));

        assertNotNull(checkGitTokenValidityInProviderStage);
        assertNotNull(checkGitTokenValidityInProviderStage.getPayload());
        assertEquals("Checking token validity at provider", checkGitTokenValidityInProviderStage.getDescription());
        assertTrue(checkGitTokenValidityInProviderStage.isSensitivePayload());

        TaskStage registerTokenInSecretsManagerStage = taskStageArgumentCaptor.getAllValues()
                .stream()
                .filter(taskStage -> taskStage.getStage().equals(TaskOperationStageEnum.REGISTER_TOKEN_IN_SECRETS_MANAGER))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Task stage %s is not found on stages", TaskOperationStageEnum.REGISTER_TOKEN_IN_SECRETS_MANAGER)));

        assertNotNull(registerTokenInSecretsManagerStage);
        assertNotNull(registerTokenInSecretsManagerStage.getPayload());
        assertEquals("Registering token in secrets manager", registerTokenInSecretsManagerStage.getDescription());
        assertTrue(registerTokenInSecretsManagerStage.isSensitivePayload());

        TaskStage persistGitCredentialInDatabaseStage = taskStageArgumentCaptor.getAllValues()
                .stream()
                .filter(taskStage -> taskStage.getStage().equals(TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Task stage %s is not found on stages", TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE)));

        assertNotNull(persistGitCredentialInDatabaseStage);
        assertNotNull(persistGitCredentialInDatabaseStage.getPayload());
        assertEquals("Persisting credentials in the database", persistGitCredentialInDatabaseStage.getDescription());
        assertFalse(persistGitCredentialInDatabaseStage.isSensitivePayload());
    }
}