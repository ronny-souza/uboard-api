package br.com.uboard.core.service;

import br.com.uboard.core.model.ScrumPokerVote;
import br.com.uboard.core.model.operations.ScrumPokerWebSocketVoteForm;
import br.com.uboard.core.model.transport.ScrumPokerVoteDTO;
import br.com.uboard.core.repository.ScrumPokerVoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class PersistScrumPokerVoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistScrumPokerVoteService.class);
    private final ScrumPokerVoteRepository scrumPokerVoteRepository;

    public PersistScrumPokerVoteService(ScrumPokerVoteRepository scrumPokerVoteRepository) {
        this.scrumPokerVoteRepository = scrumPokerVoteRepository;
    }

    @Transactional
    public ScrumPokerVoteDTO persistVote(ScrumPokerWebSocketVoteForm form) {
        LOGGER.debug("Persisting vote {} from user {} for room {}", form.vote(), form.username(), form.roomIdentifier());

        Optional<ScrumPokerVote> scrumPokerVoteAsOptional = this.scrumPokerVoteRepository.findByRoomIdentifierAndUserIdentifier(
                form.roomIdentifier(), form.userIdentifier()
        );

        if (scrumPokerVoteAsOptional.isPresent()) {
            LOGGER.debug("Vote is already created. Updating it...");
            ScrumPokerVote scrumPokerVote = scrumPokerVoteAsOptional.get();
            scrumPokerVote.setVote(StringUtils.hasText(form.vote()) ? form.vote() : scrumPokerVote.getVote());
            return new ScrumPokerVoteDTO(scrumPokerVote);
        } else {
            LOGGER.debug("Vote is not found. Creating it...");
            ScrumPokerVote scrumPokerVote = this.scrumPokerVoteRepository.save(new ScrumPokerVote(form));
            return new ScrumPokerVoteDTO(scrumPokerVote);
        }
    }
}
