package br.com.uboard.core.controller;

import br.com.uboard.core.service.SynchronizeUserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SynchronizeUserService synchronizeUserService;

    @Test
    @DisplayName("Should return status 200 on synchronize Keycloak user")
    void shouldReturnStatus200OnSynchronizeKeycloakUser() throws Exception {
        doNothing().when(this.synchronizeUserService).synchronizeUser(any());

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("DUMMY"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(this.generateJwtToken(), authorities);

        this.mockMvc.perform(get("/users/sync")
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