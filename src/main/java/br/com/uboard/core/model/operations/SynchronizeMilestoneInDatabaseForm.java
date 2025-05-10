package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.SynchronizeMilestoneFrequencyEnum;
import br.com.uboard.core.model.transport.SessionUserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SynchronizeMilestoneInDatabaseForm(@NotBlank String organizationId,
                                                 @NotNull Long milestoneId,
                                                 @NotNull SessionUserDTO user,
                                                 boolean isAutoSync,
                                                 SynchronizeMilestoneFrequencyEnum frequency,
                                                 Integer hours,
                                                 Integer minutes,
                                                 Integer weekDay) {
}
