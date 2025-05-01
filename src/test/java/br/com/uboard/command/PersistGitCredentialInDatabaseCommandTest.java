package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.User;
import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.operations.PersistGitCredentialOnDatabaseForm;
import br.com.uboard.core.repository.CredentialRepository;
import br.com.uboard.core.repository.UserRepository;
import br.com.uboard.exception.UboardApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersistGitCredentialOnDatabaseCommandTest {

    @InjectMocks
    private PersistGitCredentialOnDatabaseCommand persistGitCredentialOnDatabaseCommand;

    @Mock
    private CustomObjectMapper customObjectMapper;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Should persist the git credentials in database")
    void shouldPersistGitCredentialsInDatabase() throws UboardApplicationException {
        PersistGitCredentialOnDatabaseForm formAsMock = mock(PersistGitCredentialOnDatabaseForm.class);

        when(this.customObjectMapper.fromJson(anyString(), eq(PersistGitCredentialOnDatabaseForm.class))).thenReturn(formAsMock);
        when(formAsMock.userIdentifier()).thenReturn("userIdentifier");
        when(this.userRepository.getUserByUuid(anyString())).thenReturn(mock(User.class));
        when(formAsMock.uuid()).thenReturn("uuid");
        when(formAsMock.name()).thenReturn("name");
        when(formAsMock.url()).thenReturn("url");
        when(formAsMock.type()).thenReturn(ProviderEnum.GITLAB);

        this.persistGitCredentialOnDatabaseCommand.execute("{}");

        verify(this.credentialRepository).save(any(Credential.class));
    }
}