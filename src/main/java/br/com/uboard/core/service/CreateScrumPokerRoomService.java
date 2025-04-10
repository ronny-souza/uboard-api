package br.com.uboard.core.service;

import br.com.uboard.core.model.ScrumPokerRoom;
import br.com.uboard.core.model.operations.CreateScrumPokerRoomForm;
import br.com.uboard.core.model.transport.ScrumPokerRoomDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.ScrumPokerRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateScrumPokerRoomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateScrumPokerRoomService.class);
    private final ScrumPokerRoomRepository scrumPokerRoomRepository;

    public CreateScrumPokerRoomService(ScrumPokerRoomRepository scrumPokerRoomRepository) {
        this.scrumPokerRoomRepository = scrumPokerRoomRepository;
    }

    @Transactional
    public ScrumPokerRoomDTO createScrumPokerRoom(CreateScrumPokerRoomForm form, SessionUserDTO sessionUserDTO) {
        LOGGER.info("Starting scrum poker room creation...");
        ScrumPokerRoom scrumPokerRoom = this.scrumPokerRoomRepository.save(new ScrumPokerRoom(form, sessionUserDTO));
        return new ScrumPokerRoomDTO(scrumPokerRoom);
    }
}
