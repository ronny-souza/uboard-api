package br.com.uboard.core.service;

import br.com.uboard.core.model.ScrumPokerRoom;
import br.com.uboard.core.repository.ScrumPokerRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeScrumPokerRoomVotesVisibility {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeScrumPokerRoomVotesVisibility.class);
    private final ScrumPokerRoomRepository scrumPokerRoomRepository;

    public ChangeScrumPokerRoomVotesVisibility(ScrumPokerRoomRepository scrumPokerRoomRepository) {
        this.scrumPokerRoomRepository = scrumPokerRoomRepository;
    }

    @Transactional
    public void changeScrumPokerRoomVotesVisibility(String roomIdentifier, boolean isRoomVotesVisible) {
        LOGGER.debug("Starting changing scrum poker room {} visibility...", roomIdentifier);

        ScrumPokerRoom scrumPokerRoom = this.scrumPokerRoomRepository.findByUuid(roomIdentifier)
                .orElseThrow(() -> new IllegalArgumentException(roomIdentifier));

        scrumPokerRoom.setVotesVisible(isRoomVotesVisible);
    }
}
