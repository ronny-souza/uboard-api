package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.GitProviderEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCredentialForm(@NotBlank String name,
                                   @NotBlank String url,
                                   @NotBlank String token,
                                   @NotNull GitProviderEnum type) {
}
