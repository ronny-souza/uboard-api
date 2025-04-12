package br.com.uboard.core.controller;

import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.service.SynchronizeUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final SynchronizeUserService synchronizeUserService;

    public UserController(SynchronizeUserService synchronizeUserService) {
        this.synchronizeUserService = synchronizeUserService;
    }

    @GetMapping("/sync")
    public ResponseEntity<Void> synchronizeUser(@AuthenticationPrincipal Jwt jwt) {
        this.synchronizeUserService.synchronizeUser(new SessionUserDTO(jwt));
        return ResponseEntity.ok().build();
    }
}
