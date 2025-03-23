package br.com.uboard.core.model.operations;

public record ScrumPokerWebSocketVoteForm(String userIdentifier,
                                          String roomIdentifier,
                                          String username,
                                          String vote) {
}
