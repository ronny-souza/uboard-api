package br.com.uboard.core.controller;

import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.external.GitGroupInterface;
import br.com.uboard.core.model.external.GitProjectInterface;
import br.com.uboard.core.model.external.gitlab.GitlabGroupDTO;
import br.com.uboard.core.model.external.gitlab.GitlabProjectDTO;
import br.com.uboard.core.model.filters.CredentialFiltersDTO;
import br.com.uboard.core.model.operations.CreateCredentialForm;
import br.com.uboard.core.model.transport.CredentialDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.model.transport.TaskDTO;
import br.com.uboard.core.service.CreateTaskService;
import br.com.uboard.core.service.ListCredentialsService;
import br.com.uboard.core.service.ListGitGroupsService;
import br.com.uboard.core.service.ListGitProjectsService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CredentialController.class)
class CredentialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateTaskService createTaskService;

    @MockitoBean
    private ListCredentialsService listCredentialsService;

    @MockitoBean
    private ListGitProjectsService listGitProjectsService;

    @MockitoBean
    private ListGitGroupsService listGitGroupsService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        this.objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @DisplayName("Should return status 200 and a page of credentials")
    void shouldReturnStatus200AndPageOfCredentials() throws Exception {
        CredentialDTO credentialDTOAsMock = mock(CredentialDTO.class);
        PageImpl<CredentialDTO> credentialsPageAsMock = new PageImpl<>(List.of(credentialDTOAsMock));

        when(this.listCredentialsService
                .listCredentialsAsPage(any(Pageable.class), any(SessionUserDTO.class), any(CredentialFiltersDTO.class)))
                .thenReturn(credentialsPageAsMock);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(get("/credentials")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return status 200 and a page of projects from credential")
    void shouldReturnStatus200AndPageOfProjectsFromCredentials() throws Exception {
        GitlabProjectDTO gitProjectAsMock = mock(GitlabProjectDTO.class);
        List<GitProjectInterface> gitProjectsAsMock = List.of(gitProjectAsMock);

        when(this.listGitProjectsService
                .listGitProjects(anyString(), any(SessionUserDTO.class), anyString()))
                .thenReturn(gitProjectsAsMock);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(get("/credentials/{id}/projects", UUID.randomUUID().toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return status 200 and a page of groups from credential")
    void shouldReturnStatus200AndPageOfGroupsFromCredentials() throws Exception {
        GitlabGroupDTO gitGroupAsMock = mock(GitlabGroupDTO.class);
        List<GitGroupInterface> gitGroupsAsMock = List.of(gitGroupAsMock);

        when(this.listGitGroupsService
                .listGitGroups(anyString(), any(SessionUserDTO.class), anyString()))
                .thenReturn(gitGroupsAsMock);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(get("/credentials/{id}/groups", UUID.randomUUID().toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return status 201 and the task information on credential creation")
    void shouldReturnStatus201AndTheTaskInformationOnCredentialCreation() throws Exception {
        TaskDTO taskDTOAsMock = mock(TaskDTO.class);

        CreateCredentialForm form = new CreateCredentialForm(
                "name",
                "https://gitlab.com",
                "someSecureToken",
                ProviderEnum.GITLAB
        );

        String formAsJson = this.objectMapper.writeValueAsString(form);

        when(this.createTaskService.create(any())).thenReturn(taskDTOAsMock);
        when(taskDTOAsMock.uuid()).thenReturn("uuid");

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(post("/credentials")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(formAsJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    private Jwt generateJwtToken() {
        return Jwt.withTokenValue("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30")
                .header("alg", "HS256")
                .header("typ", "JWT")
                .claim("sub", "3a35880c-67dd-4688-a30b-3a489d22a539")
                .claim("preferred_username", "john.doe")
                .claim("email", "john.doe@example.com")
                .claim("given_name", "John")
                .claim("family_name", "Doe")
                .build();
    }
}