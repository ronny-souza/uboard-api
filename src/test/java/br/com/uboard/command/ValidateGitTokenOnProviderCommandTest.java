package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.enums.GitProviderEnum;
import br.com.uboard.core.model.external.GitUserInterface;
import br.com.uboard.core.model.operations.ValidateGitTokenOnProviderForm;
import br.com.uboard.core.service.GitService;
import br.com.uboard.exception.UboardApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateGitTokenOnProviderCommandTest {

    @InjectMocks
    private ValidateGitTokenOnProviderCommand validateGitTokenOnProviderCommand;

    @Mock
    private CustomObjectMapper customObjectMapper;

    @Mock
    private GitService gitService;

    @Test
    @DisplayName("Should execute token validation in provider")
    void shouldExecuteTokenValidationInProvider() throws UboardApplicationException {
        ValidateGitTokenOnProviderForm formAsMock = mock(ValidateGitTokenOnProviderForm.class);
        GitUserInterface gitUserAsMock = mock(GitUserInterface.class);

        when(this.customObjectMapper.fromJson(anyString(), eq(ValidateGitTokenOnProviderForm.class))).thenReturn(formAsMock);
        when(formAsMock.url()).thenReturn("url");
        when(formAsMock.token()).thenReturn("token");
        when(formAsMock.type()).thenReturn(GitProviderEnum.GITLAB);
        when(this.gitService.getCurrentUser(anyString(), anyString(), any(GitProviderEnum.class))).thenReturn(gitUserAsMock);
        when(gitUserAsMock.getUsername()).thenReturn("username");

        this.validateGitTokenOnProviderCommand.execute("{}");

        verify(this.gitService).getCurrentUser(anyString(), anyString(), any(GitProviderEnum.class));

    }
}