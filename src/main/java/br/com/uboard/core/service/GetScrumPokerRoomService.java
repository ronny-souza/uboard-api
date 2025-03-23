package br.com.uboard.core.service;

import br.com.uboard.core.model.ScrumPokerRoom;
import br.com.uboard.core.model.transport.ScrumPokerRoomDTO;
import br.com.uboard.core.repository.ScrumPokerRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetScrumPokerRoomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetScrumPokerRoomService.class);
    private final ScrumPokerRoomRepository scrumPokerRoomRepository;

    public GetScrumPokerRoomService(ScrumPokerRoomRepository scrumPokerRoomRepository) {
        this.scrumPokerRoomRepository = scrumPokerRoomRepository;
    }

    public ScrumPokerRoomDTO getScrumPokerRoom(String id) {
        LOGGER.info("Starting searching for scrum poker room from id {}...", id);
        ScrumPokerRoom scrumPokerRoom = this.scrumPokerRoomRepository.findByUuid(id)
                .orElseThrow(() -> new IllegalArgumentException(id));
        return new ScrumPokerRoomDTO(scrumPokerRoom);
    }
}
