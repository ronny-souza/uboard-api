package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.enums.ScopeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrganizationForm(@NotBlank String name,
                                     @NotNull ProviderEnum type,
                                     @NotNull ScopeEnum scope,
                                     @NotBlank String credential,
                                     @NotNull @Valid OrganizationTargetForm target) {
}
