package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.GitProviderEnum;
import br.com.uboard.core.model.operations.PersistGitCredentialOnDatabaseForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CredentialTest {

    @Test
    @DisplayName("Should return an instance from the empty constructor with the data filled in via setters")
    void shouldReturnAnInstanceFromTheEmptyConstructorWithTheDataFilledInViaSetters() {
        LocalDateTime currentDate = LocalDateTime.now();
        User userAsMock = mock(User.class);

        Credential credential = new Credential();
        credential.setId(1L);
        credential.setUuid("uuid");
        credential.setName("name");
        credential.setUrl("url");
        credential.setType(GitProviderEnum.GITLAB);
        credential.setCreatedAt(currentDate);
        credential.setUser(userAsMock);

        assertNotNull(credential);
        assertEquals(1L, credential.getId());
        assertEquals("uuid", credential.getUuid());
        assertEquals("name", credential.getName());
        assertEquals("url", credential.getUrl());
        assertEquals(GitProviderEnum.GITLAB, credential.getType());
        assertEquals(currentDate, credential.getCreatedAt());
        assertEquals(userAsMock, credential.getUser());
    }

    @Test
    @DisplayName("Should return an instance from the form args constructor")
    void shouldReturnAnInstanceFromTheFormArgsConstructor() {
        PersistGitCredentialOnDatabaseForm formAsMock = mock(PersistGitCredentialOnDatabaseForm.class);
        User userAsMock = mock(User.class);

        when(formAsMock.uuid()).thenReturn("uuid");
        when(formAsMock.name()).thenReturn("name");
        when(formAsMock.url()).thenReturn("url");
        when(formAsMock.type()).thenReturn(GitProviderEnum.GITLAB);

        Credential credential = new Credential(formAsMock, userAsMock);
        assertNotNull(credential);
        assertNotNull(credential.getCreatedAt());
        assertEquals("uuid", credential.getUuid());
        assertEquals("name", credential.getName());
        assertEquals("url", credential.getUrl());
        assertEquals(GitProviderEnum.GITLAB, credential.getType());
        assertEquals(userAsMock, credential.getUser());
    }
}