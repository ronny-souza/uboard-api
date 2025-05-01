package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.enums.ScopeEnum;
import br.com.uboard.core.model.operations.PersistOrganizationInDatabaseForm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class OrganizationIntegration extends BaseEntity {

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private String providerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderEnum type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScopeEnum scope;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public OrganizationIntegration() {

    }

    public OrganizationIntegration(PersistOrganizationInDatabaseForm form) {
        this.setUuid(UUID.randomUUID().toString());
        this.providerId = form.target().id();
        this.providerName = form.target().name();
        this.type = form.type();
        this.scope = form.scope();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getProviderId() {
        return providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public ProviderEnum getType() {
        return type;
    }

    public ScopeEnum getScope() {
        return scope;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
