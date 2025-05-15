package br.com.uboard.core.service;

import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.ScrumPokerRoom;
import br.com.uboard.core.model.User;
import br.com.uboard.core.model.operations.CreateScrumPokerRoomForm;
import br.com.uboard.core.model.transport.ScrumPokerRoomDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.MilestoneRepository;
import br.com.uboard.core.repository.ScrumPokerRoomRepository;
import br.com.uboard.core.repository.UserRepository;
import br.com.uboard.exception.MilestoneNotFoundException;
import br.com.uboard.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateScrumPokerRoomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateScrumPokerRoomService.class);
    private final ScrumPokerRoomRepository scrumPokerRoomRepository;
    private final UserRepository userRepository;
    private final MilestoneRepository milestoneRepository;

    public CreateScrumPokerRoomService(ScrumPokerRoomRepository scrumPokerRoomRepository,
                                       UserRepository userRepository,
                                       MilestoneRepository milestoneRepository) {
        this.scrumPokerRoomRepository = scrumPokerRoomRepository;
        this.userRepository = userRepository;
        this.milestoneRepository = milestoneRepository;
    }

    @Transactional
    public ScrumPokerRoomDTO createScrumPokerRoom(CreateScrumPokerRoomForm form, SessionUserDTO sessionUserDTO)
            throws UserNotFoundException, MilestoneNotFoundException {
        LOGGER.info("Starting scrum poker room creation...");
        User user = this.userRepository.getUserByUuid(sessionUserDTO.id());
        Milestone milestone = this.milestoneRepository.getMilestoneByUuid(form.milestone());
        ScrumPokerRoom scrumPokerRoom = this.scrumPokerRoomRepository.save(new ScrumPokerRoom(form, user, milestone));
        return new ScrumPokerRoomDTO(scrumPokerRoom);
    }
}
