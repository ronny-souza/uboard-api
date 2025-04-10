package br.com.uboard.core.model.operations;

import jakarta.validation.constraints.NotBlank;

public record CreateScrumPokerRoomForm(@NotBlank String name) {
}
