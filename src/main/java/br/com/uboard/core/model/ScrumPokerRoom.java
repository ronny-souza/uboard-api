package br.com.uboard.core.model;

import br.com.uboard.core.model.operations.CreateScrumPokerRoomForm;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class ScrumPokerRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private boolean closed;

    private LocalDateTime closedAt;

    private String userIdentifier;

    public ScrumPokerRoom() {

    }

    public ScrumPokerRoom(CreateScrumPokerRoomForm form) {
        this.uuid = UUID.randomUUID().toString();
        this.name = form.name();
        this.createdAt = LocalDateTime.now();
        this.closed = false;
        this.userIdentifier = form.userIdentifier();
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isClosed() {
        return closed;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }
}
