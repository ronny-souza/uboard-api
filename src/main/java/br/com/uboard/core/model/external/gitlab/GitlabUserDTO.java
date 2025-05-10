package br.com.uboard.core.model.external.gitlab;

import br.com.uboard.core.model.external.GitUserInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GitlabUserDTO implements GitUserInterface {

    private Long id;

    private String name;

    @JsonProperty("username")
    private String username;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getAvatarUrl() {
        return avatarUrl;
    }
}