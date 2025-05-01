package br.com.uboard.client;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GitClientService {

    ResponseEntity<String> getCurrentUser(Map<String, String> headers);

    ResponseEntity<String> listProjects(Map<String, String> headers, Map<String, Object> params);

    ResponseEntity<String> listGroups(Map<String, String> headers, Map<String, Object> params);
}
