package br.com.uboard.core.controller;

import br.com.uboard.core.model.operations.CreateScrumPokerRoomForm;
import br.com.uboard.core.model.transport.ScrumPokerRoomDTO;
import br.com.uboard.core.model.transport.ScrumPokerVoteDTO;
import br.com.uboard.core.service.CreateScrumPokerRoomService;
import br.com.uboard.core.service.GetScrumPokerRoomService;
import br.com.uboard.core.service.ListScrumPokerRoomVotesService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
@WebMvcTest(ScrumPokerController.class)
class ScrumPokerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateScrumPokerRoomService createScrumPokerRoomService;

    @MockitoBean
    private GetScrumPokerRoomService getScrumPokerRoomService;

    @MockitoBean
    private ListScrumPokerRoomVotesService listScrumPokerRoomVotesService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        this.objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @DisplayName("Should return status 201 and the scrum poker room created")
    void shouldReturnStatus201AndTheScrumPokerRoomCreated() throws Exception {
        ScrumPokerRoomDTO scrumPokerRoomDTOAsMock = mock(ScrumPokerRoomDTO.class);

        CreateScrumPokerRoomForm form = new CreateScrumPokerRoomForm("name");

        String formAsJson = this.objectMapper.writeValueAsString(form);

        when(this.createScrumPokerRoomService.createScrumPokerRoom(any(), any())).thenReturn(scrumPokerRoomDTOAsMock);
        when(scrumPokerRoomDTOAsMock.uuid()).thenReturn("uuid");

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(post("/scrum-poker/room")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(formAsJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Should return status 200 and the scrum poker room by identifier")
    void shouldReturnStatus200AndTheScrumPokerRoomByIdentifier() throws Exception {
        ScrumPokerRoomDTO scrumPokerRoomDTOAsMock = mock(ScrumPokerRoomDTO.class);

        when(this.getScrumPokerRoomService.getScrumPokerRoom(anyString())).thenReturn(scrumPokerRoomDTOAsMock);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(get("/scrum-poker/room/{id}", UUID.randomUUID().toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return status 200 and scrum poker room votes list")
    void shouldReturnStatus200AndTheScrumPokerRoomVotesList() throws Exception {
        List<ScrumPokerVoteDTO> votesAsMock = List.of(mock(ScrumPokerVoteDTO.class));

        when(this.listScrumPokerRoomVotesService.listScrumPokerRoomVotes(anyString())).thenReturn(votesAsMock);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(get("/scrum-poker/room/{id}/votes", UUID.randomUUID().toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
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