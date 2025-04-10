package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.GitProviderEnum;

public record ValidateGitTokenOnProviderForm(String url,
                                             String token,
                                             GitProviderEnum type) {
}
