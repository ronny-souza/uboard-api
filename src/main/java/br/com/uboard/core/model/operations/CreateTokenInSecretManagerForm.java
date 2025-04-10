package br.com.uboard.core.model.operations;

public record CreateTokenInSecretManagerForm(String userIdentifier,
                                             String credentialIdentifier,
                                             String token) {
}
