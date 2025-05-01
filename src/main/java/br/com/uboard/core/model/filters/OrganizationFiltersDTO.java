package br.com.uboard.core.model.filters;

import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.enums.ScopeEnum;

public record OrganizationFiltersDTO(String name,
                                     String providerName,
                                     ScopeEnum scope,
                                     ProviderEnum type) {
}
