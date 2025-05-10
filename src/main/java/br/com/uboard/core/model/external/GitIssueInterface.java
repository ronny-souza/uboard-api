package br.com.uboard.core.model.external;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface GitIssueInterface<T extends GitUserInterface> {
    Long getId();

    String getTitle();

    String getDescription();

    List<String> getLabels();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    default String getUrl() {
        return null;
    }

    default String getState() {
        return null;
    }

    default GitUserInterface getAuthor() {
        return null;
    }

    default List<T> getAssignees() {
        return new ArrayList<>();
    }
}
