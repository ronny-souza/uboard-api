package br.com.uboard.core.model.transport;

import br.com.uboard.core.model.Organization;

import java.time.LocalDateTime;

public record OrganizationDTO(String uuid,
                              String name,
                              OrganizationIntegrationDTO integration,
                              CredentialDTO credential,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {

    public OrganizationDTO(Organization organization) {
        this(
                organization.getUuid(),
                organization.getName(),
                new OrganizationIntegrationDTO(organization.getOrganizationIntegration()),
                new CredentialDTO(organization.getCredential()),
                organization.getCreatedAt(),
                organization.getUpdatedAt()
        );
    }
}
