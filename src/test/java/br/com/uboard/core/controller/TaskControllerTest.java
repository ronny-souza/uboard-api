package br.com.uboard.core.controller;

import br.com.uboard.core.model.enums.TaskStatusEnum;
import br.com.uboard.core.model.filters.TaskFiltersDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.model.transport.TaskDTO;
import br.com.uboard.core.model.transport.TaskProgressDTO;
import br.com.uboard.core.service.GetTaskService;
import br.com.uboard.core.service.ListTasksService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListTasksService listTasksService;

    @MockitoBean
    private GetTaskService getTaskService;

    @Test
    @DisplayName("Should return status 200 and tasks page")
    void shouldReturnStatus200AndTasksPage() throws Exception {
        TaskDTO taskDTOAsMock = mock(TaskDTO.class);
        PageImpl<TaskDTO> tasksPageAsMock = new PageImpl<>(List.of(taskDTOAsMock));

        when(this.listTasksService
                .listTasksAsPage(any(Pageable.class), any(SessionUserDTO.class), any(TaskFiltersDTO.class)))
                .thenReturn(tasksPageAsMock);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(get("/tasks")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return status 200 and single task progress")
    void shouldReturnStatus200AndSingleTaskProgress() throws Exception {
        TaskProgressDTO taskProgressDTOAsMock = new TaskProgressDTO() {
            @Override
            public TaskStatusEnum getStatus() {
                return TaskStatusEnum.COMPLETED;
            }

            @Override
            public Integer getProgress() {
                return 100;
            }
        };

        when(this.getTaskService
                .getTaskProgress(anyString())).thenReturn(taskProgressDTOAsMock);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(get("/tasks/{uuid}/progress", UUID.randomUUID().toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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