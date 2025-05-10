package br.com.uboard.core.model.external.gitlab;

import br.com.uboard.core.model.external.GitMilestoneInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class GitlabMilestoneDTO implements GitMilestoneInterface {
    private Long id;

    private String title;

    private String description;

    private String state;

    @JsonProperty("due_date")
    private LocalDate dueDate;

    @JsonProperty("start_date")
    private LocalDate startDate;

    private boolean expired;

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
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
