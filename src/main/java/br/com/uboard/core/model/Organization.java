package br.com.uboard.core.model;

import br.com.uboard.core.model.operations.PersistOrganizationInDatabaseForm;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Organization extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "integration_id", nullable = false)
    private OrganizationIntegration organizationIntegration;

    @ManyToOne
    @JoinColumn(name = "credential_id", nullable = false)
    private Credential credential;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Organization() {
    }

    public Organization(PersistOrganizationInDatabaseForm form,
                        Credential credential,
                        User user) {
        this.setUuid(form.uuid());
        this.name = form.name();
        this.organizationIntegration = new OrganizationIntegration(form);
        this.credential = credential;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrganizationIntegration getOrganizationIntegration() {
        return organizationIntegration;
    }

    public void setOrganizationIntegration(OrganizationIntegration organizationIntegration) {
        this.organizationIntegration = organizationIntegration;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
