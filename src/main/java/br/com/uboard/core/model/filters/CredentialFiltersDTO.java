package br.com.uboard.core.model.filters;

import br.com.uboard.core.model.enums.GitProviderEnum;

public record CredentialFiltersDTO(String name,
                                   String url,
                                   GitProviderEnum type) {
}
