package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.GitProviderEnum;
import br.com.uboard.core.model.operations.PersistGitCredentialOnDatabaseForm;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GitProviderEnum type;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String userIdentifier;

    public Credential() {

    }

    public Credential(PersistGitCredentialOnDatabaseForm form) {
        this.uuid = form.uuid();
        this.name = form.name();
        this.url = form.url();
        this.type = form.type();
        this.createdAt = LocalDateTime.now();
        this.userIdentifier = form.userIdentifier();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public GitProviderEnum getType() {
        return type;
    }

    public void setType(GitProviderEnum type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }
}
