package br.com.uboard.core.model.transport;

import br.com.uboard.core.model.ScrumPokerRoom;

import java.time.LocalDateTime;

public record ScrumPokerRoomDTO(String uuid,
                                String name,
                                LocalDateTime createdAt,
                                boolean closed,
                                LocalDateTime closedAt) {

    public ScrumPokerRoomDTO(ScrumPokerRoom scrumPokerRoom) {
        this(
                scrumPokerRoom.getUuid(),
                scrumPokerRoom.getName(),
                scrumPokerRoom.getCreatedAt(),
                scrumPokerRoom.isClosed(),
                scrumPokerRoom.getClosedAt()
        );
    }
}
