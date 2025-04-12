package br.com.uboard.core.model.external.gitlab;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GitlabUserDTOTest {

    @Test
    @DisplayName("Should return a valid instance")
    void shouldReturnValidInstance() {
        GitlabUserDTO gitlabUserDTO = new GitlabUserDTO();
        gitlabUserDTO.setUsername("username");

        assertNotNull(gitlabUserDTO);
        assertEquals("username", gitlabUserDTO.getUsername());
    }
}