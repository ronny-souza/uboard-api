package br.com.uboard.core.model.external.gitlab;

import br.com.uboard.core.model.external.GitIssueInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class GitlabIssueDTO implements GitIssueInterface<GitlabUserDTO> {

    @JsonProperty("iid")
    private Long id;

    private String title;

    private String description;

    private List<String> labels;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("web_url")
    private String url;

    private String state;

    private GitlabUserDTO author;

    private List<GitlabUserDTO> assignees;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public GitlabUserDTO getAuthor() {
        return author;
    }

    public void setAuthor(GitlabUserDTO author) {
        this.author = author;
    }

    @Override
    public List<GitlabUserDTO> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<GitlabUserDTO> assignees) {
        this.assignees = assignees;
    }
}
