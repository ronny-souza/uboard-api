package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.operations.CreateTokenInSecretManagerForm;
import br.com.uboard.core.service.VaultService;
import br.com.uboard.exception.UboardApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTokenInSecretManagerCommandTest {

    @InjectMocks
    private CreateTokenInSecretManagerCommand createTokenInSecretManagerCommand;

    @Mock
    private CustomObjectMapper customObjectMapper;

    @Mock
    private VaultService vaultService;

    @Test
    @DisplayName("Should register the token in Vault")
    void shouldRegisterTokenInVault() throws UboardApplicationException {
        CreateTokenInSecretManagerForm formAsMock = mock(CreateTokenInSecretManagerForm.class);

        when(this.customObjectMapper.fromJson(anyString(), Mockito.eq(CreateTokenInSecretManagerForm.class))).thenReturn(formAsMock);
        when(formAsMock.token()).thenReturn("token");
        when(formAsMock.userIdentifier()).thenReturn("123");
        when(formAsMock.credentialIdentifier()).thenReturn("321");

        ArgumentCaptor<String> vaultPathArgumentCaptor = forClass(String.class);
        this.createTokenInSecretManagerCommand.execute("{}");

        verify(this.vaultService).addSecret(vaultPathArgumentCaptor.capture(), anyMap());

        String expectedPath = String.format("users/%s/credentials/%s", "123", "321");
        String path = vaultPathArgumentCaptor.getValue();

        assertEquals(expectedPath, path);
    }

}