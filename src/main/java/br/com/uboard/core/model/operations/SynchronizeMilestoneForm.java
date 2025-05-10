package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.SynchronizeMilestoneFrequencyEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SynchronizeMilestoneForm(String organization,
                                       @NotNull @Valid OrganizationMilestoneForm milestone,
                                       boolean isAutoSync,
                                       boolean isImporting,
                                       SynchronizeMilestoneFrequencyEnum frequency,
                                       Integer hours,
                                       Integer minutes,
                                       Integer weekDay) {
}
