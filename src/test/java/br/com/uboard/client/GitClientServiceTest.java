package br.com.uboard.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GitClientServiceTest {

    private final GitClientService gitClientService = new GitClientService() {
        @Override
        public ResponseEntity<String> getCurrentUser(Map<String, String> headers) {
            return null;
        }
    };

    @Test
    @DisplayName("Should return method not allowed when listing projects")
    void shouldReturnMethodNotAllowedWhenListingProjects() {
        ResponseEntity<String> response = this.gitClientService.listProjects(Collections.emptyMap(), Collections.emptyMap());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return method not allowed when listing groups")
    void shouldReturnMethodNotAllowedWhenListingGroups() {
        ResponseEntity<String> response = this.gitClientService.listGroups(Collections.emptyMap(), Collections.emptyMap());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return method not allowed when listing project milestones")
    void shouldReturnMethodNotAllowedWhenListingProjectMilestones() {
        ResponseEntity<String> response = this.gitClientService.listProjectMilestones("1", Collections.emptyMap(), Collections.emptyMap());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return method not allowed when listing group milestones")
    void shouldReturnMethodNotAllowedWhenListingGroupMilestones() {
        ResponseEntity<String> response = this.gitClientService.listGroupMilestones("1", Collections.emptyMap(), Collections.emptyMap());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return method not allowed when listing project milestone issues")
    void shouldReturnMethodNotAllowedWhenListingProjectMilestoneIssues() {
        ResponseEntity<String> response = this.gitClientService.listProjectMilestoneIssues("1", Collections.emptyMap(), Collections.emptyMap());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return method not allowed when listing group milestone issues")
    void shouldReturnMethodNotAllowedWhenListingGroupMilestoneIssues() {
        ResponseEntity<String> response = this.gitClientService.listGroupMilestoneIssues("1", Collections.emptyMap(), Collections.emptyMap());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return method not allowed when getting single project milestone")
    void shouldReturnMethodNotAllowedWhenGettingSingleProjectMilestone() {
        ResponseEntity<String> response = this.gitClientService.getSingleProjectMilestone("1", "1", Collections.emptyMap());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return method not allowed when getting single group milestone")
    void shouldReturnMethodNotAllowedWhenGettingSingleGroupMilestone() {
        ResponseEntity<String> response = this.gitClientService.getSingleGroupMilestone("1", "1", Collections.emptyMap());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }
}