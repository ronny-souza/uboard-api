package br.com.uboard.core.service;

import br.com.uboard.core.model.transport.ScrumPokerVoteDTO;
import br.com.uboard.core.repository.ScrumPokerVoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListScrumPokerRoomVotesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListScrumPokerRoomVotesService.class);
    private final ScrumPokerVoteRepository scrumPokerVoteRepository;

    public ListScrumPokerRoomVotesService(ScrumPokerVoteRepository scrumPokerVoteRepository) {
        this.scrumPokerVoteRepository = scrumPokerVoteRepository;
    }

    public List<ScrumPokerVoteDTO> listScrumPokerRoomVotes(String id) {
        LOGGER.info("Starting listing scrum poker room votes for room {}...", id);
        return this.scrumPokerVoteRepository.findByRoomIdentifier(id).stream().map(ScrumPokerVoteDTO::new).toList();
    }
}
