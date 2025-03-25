package br.com.uboard.core.service;

import br.com.uboard.core.repository.ScrumPokerVoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteScrumPokerRoomUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteScrumPokerRoomUserService.class);
    private final ScrumPokerVoteRepository scrumPokerVoteRepository;

    public DeleteScrumPokerRoomUserService(ScrumPokerVoteRepository scrumPokerVoteRepository) {
        this.scrumPokerVoteRepository = scrumPokerVoteRepository;
    }

    @Transactional
    public void deleteUserVote(String roomIdentifier, String userIdentifier) {
        LOGGER.debug("Starting user {} deletion from room {}", userIdentifier, roomIdentifier);
        this.scrumPokerVoteRepository.deleteByRoomIdentifierAndUserIdentifier(roomIdentifier, userIdentifier);
    }
}
