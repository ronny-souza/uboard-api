package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.ProviderEnum;

public record ValidateGitTokenOnProviderForm(String url,
                                             String token,
                                             ProviderEnum type) {
}
