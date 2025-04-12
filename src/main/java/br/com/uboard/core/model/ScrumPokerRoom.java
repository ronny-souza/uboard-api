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
        this.uuid = UUID.randomUUID().toString();
        this.name = form.name();
        this.createdAt = LocalDateTime.now();
        this.closed = false;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
