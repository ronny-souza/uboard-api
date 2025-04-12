package br.com.uboard.core.model;

import br.com.uboard.core.model.operations.CreateScrumPokerRoomForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScrumPokerRoomTest {

    @Test
    @DisplayName("Should return an instance from the empty constructor with the data filled in via setters")
    void shouldReturnAnInstanceFromTheEmptyConstructorWithTheDataFilledInViaSetters() {
        User userAsMock = mock(User.class);

        ScrumPokerRoom scrumPokerRoom = new ScrumPokerRoom();
        scrumPokerRoom.setId(1L);
        scrumPokerRoom.setUuid("uuid");
        scrumPokerRoom.setName("name");
        scrumPokerRoom.setClosed(false);
        scrumPokerRoom.setClosedAt(null);
        scrumPokerRoom.setUser(userAsMock);
        scrumPokerRoom.setVotesVisible(false);

        assertNotNull(scrumPokerRoom);
        assertNotNull(scrumPokerRoom.getCreatedAt());
        assertNull(scrumPokerRoom.getClosedAt());
        assertFalse(scrumPokerRoom.isClosed());
        assertFalse(scrumPokerRoom.isVotesVisible());
        assertEquals(1L, scrumPokerRoom.getId());
        assertEquals("uuid", scrumPokerRoom.getUuid());
        assertEquals("name", scrumPokerRoom.getName());
        assertEquals(userAsMock, scrumPokerRoom.getUser());
    }

    @Test
    @DisplayName("Should return an instance from the form args constructor")
    void shouldReturnAnInstanceFromTheFormArgsConstructor() {
        CreateScrumPokerRoomForm formAsMock = mock(CreateScrumPokerRoomForm.class);
        User userAsMock = mock(User.class);

        when(formAsMock.name()).thenReturn("name");

        ScrumPokerRoom scrumPokerRoom = new ScrumPokerRoom(formAsMock, userAsMock);

        assertNotNull(scrumPokerRoom);
        assertNotNull(scrumPokerRoom.getCreatedAt());
        assertNotNull(scrumPokerRoom.getUuid());
        assertNull(scrumPokerRoom.getClosedAt());
        assertFalse(scrumPokerRoom.isClosed());
        assertFalse(scrumPokerRoom.isVotesVisible());
        assertEquals("name", scrumPokerRoom.getName());
        assertEquals(userAsMock, scrumPokerRoom.getUser());
    }
}