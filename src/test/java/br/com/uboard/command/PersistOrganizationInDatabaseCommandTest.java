package br.com.uboard.command;

import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.User;
import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.enums.ScopeEnum;
import br.com.uboard.core.model.operations.OrganizationTargetForm;
import br.com.uboard.core.model.operations.PersistOrganizationInDatabaseForm;
import br.com.uboard.core.repository.CredentialRepository;
import br.com.uboard.core.repository.OrganizationRepository;
import br.com.uboard.core.repository.UserRepository;
import br.com.uboard.exception.UboardApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersistOrganizationInDatabaseCommandTest {

    @InjectMocks
    private PersistOrganizationInDatabaseCommand persistOrganizationInDatabaseCommand;

    @Mock
    private CustomObjectMapper customObjectMapper;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Should persist the organization in database")
    void shouldPersistTheOrganizationInDatabase() throws UboardApplicationException {
        PersistOrganizationInDatabaseForm formAsMock = mock(PersistOrganizationInDatabaseForm.class);
        OrganizationTargetForm targetFormAsMock = mock(OrganizationTargetForm.class);
        Credential credentialAsMock = mock(Credential.class);
        User userAsMock = mock(User.class);

        when(this.customObjectMapper.fromJson(anyString(), eq(PersistOrganizationInDatabaseForm.class))).thenReturn(formAsMock);
        when(formAsMock.credential()).thenReturn("credential");
        when(formAsMock.user()).thenReturn("user");
        when(this.credentialRepository.findByUuidAndUserUuid(anyString(), anyString())).thenReturn(Optional.of(credentialAsMock));
        when(this.userRepository.getUserByUuid(anyString())).thenReturn(userAsMock);
        when(formAsMock.uuid()).thenReturn("uuid");
        when(formAsMock.name()).thenReturn("name");
        when(formAsMock.target()).thenReturn(targetFormAsMock);
        when(targetFormAsMock.id()).thenReturn(1L);
        when(targetFormAsMock.name()).thenReturn("name");
        when(formAsMock.type()).thenReturn(ProviderEnum.GITLAB);
        when(formAsMock.scope()).thenReturn(ScopeEnum.GROUP);

        this.persistOrganizationInDatabaseCommand.execute("{}");

        verify(this.organizationRepository).save(any(Organization.class));
    }
}