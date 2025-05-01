package br.com.uboard.configuration;

import br.com.uboard.core.model.properties.ApplicationConfigurationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigurationTest {

    private SecurityConfiguration securityConfiguration;

    @Mock
    private ApplicationConfigurationProperties applicationConfigurationProperties;

    @BeforeEach
    void init() {
        this.securityConfiguration = new SecurityConfiguration(applicationConfigurationProperties);
    }

    @Test
    @DisplayName("Should return an instance of SecurityFilterChain")
    void shouldReturnAnInstanceOfSecurityFilterChain() throws Exception {
        HttpSecurity httpSecurityAsMock = mock(HttpSecurity.class);
        when(httpSecurityAsMock.cors(any())).thenReturn(httpSecurityAsMock);
        when(httpSecurityAsMock.csrf(any())).thenReturn(httpSecurityAsMock);
        when(httpSecurityAsMock.authorizeHttpRequests(any())).thenReturn(httpSecurityAsMock);
        when(httpSecurityAsMock.oauth2ResourceServer(any())).thenReturn(httpSecurityAsMock);
        when(httpSecurityAsMock.sessionManagement(any())).thenReturn(httpSecurityAsMock);
        when(httpSecurityAsMock.build()).thenReturn(mock(DefaultSecurityFilterChain.class));
        SecurityFilterChain securityFilterChain = this.securityConfiguration.filterChain(httpSecurityAsMock);
        assertNotNull(securityFilterChain);
    }

    @Test
    @DisplayName("Should return the CORS configuration")
    void shouldReturnCorsConfiguration() {
        when(this.applicationConfigurationProperties.getAllowedOrigins()).thenReturn(List.of("http://localhost:4200"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = this.securityConfiguration.corsConfigurationSource();
        assertNotNull(urlBasedCorsConfigurationSource);
    }
}