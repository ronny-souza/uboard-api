package br.com.uboard.core.model;

import br.com.uboard.core.model.operations.CreateScrumPokerRoomForm;
import br.com.uboard.core.model.transport.SessionUserDTO;
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

    @Column(nullable = false)
    private String userIdentifier;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isVotesVisible;

    public ScrumPokerRoom() {

    }

    public ScrumPokerRoom(CreateScrumPokerRoomForm form, SessionUserDTO sessionUserDTO) {
        this.uuid = UUID.randomUUID().toString();
        this.name = form.name();
        this.createdAt = LocalDateTime.now();
        this.closed = false;
        this.userIdentifier = sessionUserDTO.id();
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

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public boolean isVotesVisible() {
        return isVotesVisible;
    }

    public void setVotesVisible(boolean votesVisible) {
        isVotesVisible = votesVisible;
    }
}
