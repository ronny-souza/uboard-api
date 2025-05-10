package br.com.uboard.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GitClientService {

    ResponseEntity<String> getCurrentUser(Map<String, String> headers);

    default ResponseEntity<String> listProjects(Map<String, String> headers, Map<String, Object> params) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    default ResponseEntity<String> listGroups(Map<String, String> headers, Map<String, Object> params) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    default ResponseEntity<String> listProjectMilestones(String id, Map<String, String> headers, Map<String, Object> params) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    default ResponseEntity<String> listGroupMilestones(String id, Map<String, String> headers, Map<String, Object> params) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    default ResponseEntity<String> listProjectMilestoneIssues(String id, Map<String, String> headers, Map<String, Object> params) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    default ResponseEntity<String> listGroupMilestoneIssues(String id, Map<String, String> headers, Map<String, Object> params) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    default ResponseEntity<String> getSingleProjectMilestone(String projectId, String milestoneId, Map<String, String> headers) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    default ResponseEntity<String> getSingleGroupMilestone(String groupId, String milestoneId, Map<String, String> headers) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }
}
