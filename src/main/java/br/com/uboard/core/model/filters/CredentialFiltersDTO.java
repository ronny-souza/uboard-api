package br.com.uboard.core.model.filters;

import br.com.uboard.core.model.enums.ProviderEnum;

public record CredentialFiltersDTO(String name,
                                   String url,
                                   ProviderEnum type) {
}
