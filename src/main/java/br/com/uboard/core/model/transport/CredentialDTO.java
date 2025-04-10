package br.com.uboard.core.model.transport;

import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.enums.GitProviderEnum;

import java.time.LocalDateTime;

public record CredentialDTO(String uuid,
                            String name,
                            String url,
                            GitProviderEnum type,
                            LocalDateTime createdAt) {

    public CredentialDTO(Credential credential) {
        this(
                credential.getUuid(),
                credential.getName(),
                credential.getUrl(),
                credential.getType(),
                credential.getCreatedAt()
        );
    }
}
