package br.com.uboard.core.model.transport;

import br.com.uboard.core.model.OrganizationIntegration;
import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.enums.ScopeEnum;

import java.time.LocalDateTime;

public record OrganizationIntegrationDTO(String providerName,
                                         ProviderEnum type,
                                         ScopeEnum scope,
                                         LocalDateTime createdAt,
                                         LocalDateTime updatedAt) {

    public OrganizationIntegrationDTO(OrganizationIntegration organizationIntegration) {
        this(
                organizationIntegration.getProviderName(),
                organizationIntegration.getType(),
                organizationIntegration.getScope(),
                organizationIntegration.getCreatedAt(),
                organizationIntegration.getUpdatedAt()
        );
    }
}
