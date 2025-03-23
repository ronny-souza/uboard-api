package br.com.uboard.core.model;

import br.com.uboard.core.model.operations.ScrumPokerWebSocketVoteForm;
import jakarta.persistence.*;

@Entity
public class ScrumPokerVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userIdentifier;

    @Column(nullable = false)
    private String roomIdentifier;

    @Column(nullable = false)
    private String username;

    private String vote;

    public ScrumPokerVote() {

    }

    public ScrumPokerVote(ScrumPokerWebSocketVoteForm form) {
        this.userIdentifier = form.userIdentifier();
        this.roomIdentifier = form.roomIdentifier();
        this.username = form.username();
        this.vote = form.vote();
    }

    public Long getId() {
        return id;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public String getRoomIdentifier() {
        return roomIdentifier;
    }

    public String getUsername() {
        return username;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
