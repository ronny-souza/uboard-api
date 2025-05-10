package br.com.uboard.core.model.external;

public interface GitUserInterface {

    Long getId();

    String getName();

    String getUsername();

    default String getAvatarUrl() {
        return null;
    }
}
