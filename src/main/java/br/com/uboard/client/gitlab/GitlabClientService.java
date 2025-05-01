package br.com.uboard.client.gitlab;

import br.com.uboard.client.GitClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface GitlabClientService extends GitClientService {

    @Override
    @GetMapping("/user")
    ResponseEntity<String> getCurrentUser(@RequestHeader Map<String, String> headers);

    @Override
    @GetMapping("/projects")
    ResponseEntity<String> listProjects(@RequestHeader Map<String, String> headers, @RequestParam Map<String, Object> params);

    @Override
    @GetMapping("/groups")
    ResponseEntity<String> listGroups(@RequestHeader Map<String, String> headers, @RequestParam Map<String, Object> params);
}
