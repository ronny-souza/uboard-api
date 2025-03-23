package br.com.uboard.core.model.transport;

import br.com.uboard.core.model.ScrumPokerVote;

public record ScrumPokerVoteDTO(String userIdentifier,
                                String roomIdentifier,
                                String username,
                                String vote) {

    public ScrumPokerVoteDTO(ScrumPokerVote scrumPokerVote) {
        this(
                scrumPokerVote.getUserIdentifier(),
                scrumPokerVote.getRoomIdentifier(),
                scrumPokerVote.getUsername(),
                scrumPokerVote.getVote()
        );
    }
}
