package br.com.uboard.core.repository;

import br.com.uboard.core.model.ScrumPokerVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrumPokerVoteRepository extends JpaRepository<ScrumPokerVote, Long> {

    List<ScrumPokerVote> findByRoomIdentifier(String roomIdentifier);

    Optional<ScrumPokerVote> findByRoomIdentifierAndUserIdentifier(String roomIdentifier, String userIdentifier);
}
