package br.com.uboard.core.model.external.gitlab;

import br.com.uboard.core.model.external.GitGroupInterface;

public class GitlabGroupDTO implements GitGroupInterface {
    private Long id;
    private String name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
