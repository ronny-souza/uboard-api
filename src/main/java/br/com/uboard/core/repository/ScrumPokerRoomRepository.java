package br.com.uboard.core.repository;

import br.com.uboard.core.model.ScrumPokerRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrumPokerRoomRepository extends JpaRepository<ScrumPokerRoom, Long> {

    Optional<ScrumPokerRoom> findByUuid(String uuid);

    Optional<ScrumPokerRoom> findByUuidAndUserIdentifier(String uuid, String userIdentifier);

}

