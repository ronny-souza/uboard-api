package br.com.uboard.core.model.transport;

import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.enums.MilestoneStateEnum;
import br.com.uboard.core.model.enums.SynchronizeMilestoneFrequencyEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MilestoneDTO(String uuid,
                           Long providerId,
                           String title,
                           MilestoneStateEnum state,
                           LocalDate createdAt,
                           LocalDate finishedAt,
                           LocalDateTime synchronizedAt,
                           SynchronizeMilestoneFrequencyEnum frequency,
                           Integer hours,
                           Integer minutes,
                           Integer weekDay) {

    public MilestoneDTO(Milestone milestone) {
        this(
                milestone.getUuid(),
                milestone.getProviderId(),
                milestone.getTitle(),
                milestone.getState(),
                milestone.getCreatedAt(),
                milestone.getFinishedAt(),
                milestone.getSynchronizedAt(),
                milestone.getFrequency(),
                milestone.getHours(),
                milestone.getMinutes(),
                milestone.getWeekDay()
        );
    }
}
