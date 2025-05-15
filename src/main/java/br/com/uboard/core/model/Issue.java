package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.IssueStateEnum;
import br.com.uboard.core.model.external.GitIssueInterface;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Issue extends BaseEntity {

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private IssueUser author;

    @ElementCollection
    @CollectionTable(name = "issue_labels", joinColumns = @JoinColumn(name = "issue_id"))
    private List<String> labels = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private IssueStateEnum state;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isVoted;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "issue_assignees",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "assignee_id")
    )
    private Set<IssueUser> assignees = new HashSet<>();

    public Issue() {
    }

    public Issue(GitIssueInterface<?> issue, Milestone milestone) {
        this.setUuid(UUID.randomUUID().toString());
        this.setProviderId(issue.getId());
        this.setTitle(issue.getTitle());
        this.setDescription(issue.getDescription());
        this.setUrl(issue.getUrl());
        this.setLabels(issue.getLabels());
        this.setState(issue.getState() != null ? IssueStateEnum.valueOf(issue.getState().toUpperCase()) : null);
        this.setCreatedAt(issue.getCreatedAt());
        this.setUpdatedAt(issue.getUpdatedAt());
        this.setMilestone(milestone);
        this.checkIsVoted();
    }

    public void checkIsVoted() {
        this.isVoted = this.labels != null &&
                this.labels.stream().anyMatch(label -> label.trim().matches("^\\d+$"));
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IssueUser getAuthor() {
        return author;
    }

    public void setAuthor(IssueUser author) {
        this.author = author;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public IssueStateEnum getState() {
        return state;
    }

    public void setState(IssueStateEnum state) {
        this.state = state;
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

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public Set<IssueUser> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<IssueUser> assignees) {
        this.assignees = assignees;
    }
}
