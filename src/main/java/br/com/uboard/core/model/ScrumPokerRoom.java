package br.com.uboard.core.model;

import br.com.uboard.core.model.operations.CreateScrumPokerRoomForm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class ScrumPokerRoom extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private final LocalDateTime createdAt;

    private boolean closed;

    private LocalDateTime closedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isVotesVisible;

    public ScrumPokerRoom() {
        this.createdAt = LocalDateTime.now();
    }

    public ScrumPokerRoom(CreateScrumPokerRoomForm form, User user) {
        setUuid(UUID.randomUUID().toString());
        this.name = form.name();
        this.createdAt = LocalDateTime.now();
        this.closed = false;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isVotesVisible() {
        return isVotesVisible;
    }

    public void setVotesVisible(boolean votesVisible) {
        isVotesVisible = votesVisible;
    }
}
