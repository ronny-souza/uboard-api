package br.com.uboard.core.model.operations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrganizationMilestoneForm(@NotNull Long id,
                                        @NotBlank String title) {
}
