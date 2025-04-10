package br.com.uboard.core.model.transport;

import org.springframework.security.oauth2.jwt.Jwt;

public record SessionUserDTO(String id,
                             String username,
                             String email,
                             String firstName,
                             String lastName) {

    public SessionUserDTO(Jwt jwt) {
        this(
                jwt.getClaimAsString("sub"),
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name")
        );
    }
}
