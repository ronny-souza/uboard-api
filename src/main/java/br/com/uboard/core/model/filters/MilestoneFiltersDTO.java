package br.com.uboard.core.model.filters;

import br.com.uboard.core.model.enums.MilestoneStateEnum;

public record MilestoneFiltersDTO(String title,
                                  MilestoneStateEnum state) {
}
