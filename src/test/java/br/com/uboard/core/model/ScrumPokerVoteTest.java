package br.com.uboard.core.model;

import br.com.uboard.core.model.operations.ScrumPokerWebSocketVoteForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScrumPokerVoteTest {

    @Test
    @DisplayName("Should return an instance from the empty constructor with the data filled in via setters")
    void shouldReturnAnInstanceFromTheEmptyConstructorWithTheDataFilledInViaSetters() {
        ScrumPokerVote scrumPokerVote = new ScrumPokerVote();
        scrumPokerVote.setId(1L);
        scrumPokerVote.setUserIdentifier("userIdentifier");
        scrumPokerVote.setRoomIdentifier("roomIdentifier");
        scrumPokerVote.setUsername("username");
        scrumPokerVote.setVote("1");

        assertNotNull(scrumPokerVote);
        assertEquals(1L, scrumPokerVote.getId());
        assertEquals("userIdentifier", scrumPokerVote.getUserIdentifier());
        assertEquals("roomIdentifier", scrumPokerVote.getRoomIdentifier());
        assertEquals("username", scrumPokerVote.getUsername());
        assertEquals("1", scrumPokerVote.getVote());
    }

    @Test
    @DisplayName("Should return an instance from the form args constructor")
    void shouldReturnAnInstanceFromTheFormArgsConstructor() {
        ScrumPokerWebSocketVoteForm formAsMock = mock(ScrumPokerWebSocketVoteForm.class);

        when(formAsMock.userIdentifier()).thenReturn("userIdentifier");
        when(formAsMock.roomIdentifier()).thenReturn("roomIdentifier");
        when(formAsMock.username()).thenReturn("username");
        when(formAsMock.vote()).thenReturn("1");

        ScrumPokerVote scrumPokerVote = new ScrumPokerVote(formAsMock);
        assertNotNull(scrumPokerVote);
        assertEquals("userIdentifier", scrumPokerVote.getUserIdentifier());
        assertEquals("roomIdentifier", scrumPokerVote.getRoomIdentifier());
        assertEquals("username", scrumPokerVote.getUsername());
        assertEquals("1", scrumPokerVote.getVote());
    }
}