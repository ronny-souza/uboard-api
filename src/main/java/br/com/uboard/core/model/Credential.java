package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.operations.PersistGitCredentialInDatabaseForm;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Credential extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderEnum type;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Credential() {

    }

    public Credential(PersistGitCredentialInDatabaseForm form, User user) {
        setUuid(form.uuid());
        this.name = form.name();
        this.url = form.url();
        this.type = form.type();
        this.createdAt = LocalDateTime.now();
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProviderEnum getType() {
        return type;
    }

    public void setType(ProviderEnum type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
