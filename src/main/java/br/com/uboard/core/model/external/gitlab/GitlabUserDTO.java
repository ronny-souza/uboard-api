package br.com.uboard.core.model.external.gitlab;

import br.com.uboard.core.model.external.GitUserInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GitlabUserDTO implements GitUserInterface {

    @JsonProperty("username")
    private String username;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
