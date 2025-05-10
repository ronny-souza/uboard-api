package br.com.uboard.core.model;

import br.com.uboard.core.model.external.GitUserInterface;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class IssueUser extends BaseEntity {

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    private String avatarUrl;

    @ManyToMany(mappedBy = "assignees")
    private List<Issue> issues = new ArrayList<>();

    public IssueUser() {
    }

    public IssueUser(GitUserInterface user) {
        this.setUuid(UUID.randomUUID().toString());
        this.setProviderId(user.getId());
        this.setUsername(user.getUsername());
        this.setName(user.getName());
        this.setAvatarUrl(user.getAvatarUrl());
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<Issue> getIssues() {
        return issues;
    }
}
