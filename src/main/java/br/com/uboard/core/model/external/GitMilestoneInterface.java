package br.com.uboard.core.model.external;

import java.time.LocalDate;

public interface GitMilestoneInterface {
    Long getId();

    String getTitle();

    String getDescription();

    LocalDate getDueDate();

    LocalDate getStartDate();

    default String getState() {
        return null;
    }

    default boolean isExpired() {
        return false;
    }
}
