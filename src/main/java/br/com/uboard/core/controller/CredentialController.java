package br.com.uboard.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credentials")
public class CredentialController {

    @GetMapping
    public ResponseEntity<String> listCredentials() {
        return ResponseEntity.ok("{ \"value\": \"Tudo certo!\"}");
    }
}
