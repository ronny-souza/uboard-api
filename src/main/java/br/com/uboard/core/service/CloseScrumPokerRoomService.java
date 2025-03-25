package br.com.uboard.core.service;

import br.com.uboard.core.model.ScrumPokerRoom;
import br.com.uboard.core.repository.ScrumPokerRoomRepository;
import br.com.uboard.exception.ScrumPokerRoomNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CloseScrumPokerRoomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseScrumPokerRoomService.class);
    private final ScrumPokerRoomRepository scrumPokerRoomRepository;

    public CloseScrumPokerRoomService(ScrumPokerRoomRepository scrumPokerRoomRepository) {
        this.scrumPokerRoomRepository = scrumPokerRoomRepository;
    }

    @Transactional
    public void closeRoom(String roomIdentifier, String userIdentifier) throws ScrumPokerRoomNotFoundException {
        LOGGER.debug("Starting closing scrum poker room {}...", roomIdentifier);

        ScrumPokerRoom scrumPokerRoom = this.scrumPokerRoomRepository.findByUuidAndUserIdentifier(roomIdentifier, userIdentifier)
                .orElseThrow(() -> new ScrumPokerRoomNotFoundException(
                                String.format("Scrum poker room %s is not found for this user %s",
                                        roomIdentifier,
                                        userIdentifier
                                )
                        )
                );

        scrumPokerRoom.setClosed(true);
        scrumPokerRoom.setClosedAt(LocalDateTime.now());
    }
}
