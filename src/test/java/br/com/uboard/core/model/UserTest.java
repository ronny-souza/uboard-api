package br.com.uboard.core.model;

import br.com.uboard.core.model.transport.SessionUserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {

    @Test
    @DisplayName("Should return an instance from the empty constructor with the data filled in via setters")
    void shouldReturnAnInstanceFromTheEmptyConstructorWithTheDataFilledInViaSetters() {
        User user = new User();
        user.setId(1L);
        user.setUuid("uuid");
        user.setUsername("username");
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        assertEquals(1L, user.getId());
        assertEquals("uuid", user.getUuid());
        assertEquals("username", user.getUsername());
        assertEquals("email", user.getEmail());
        assertEquals("firstName", user.getFirstName());
        assertEquals("lastName", user.getLastName());
    }

    @Test
    @DisplayName("Should return an instance from the session user args constructor")
    void shouldReturnAnInstanceFromTheSessionUserArgsConstructor() {
        SessionUserDTO sessionUserAsMock = mock(SessionUserDTO.class);
        when(sessionUserAsMock.id()).thenReturn("uuid");
        when(sessionUserAsMock.username()).thenReturn("username");
        when(sessionUserAsMock.email()).thenReturn("email");
        when(sessionUserAsMock.firstName()).thenReturn("firstName");
        when(sessionUserAsMock.lastName()).thenReturn("lastName");
        User user = new User(sessionUserAsMock);

        assertEquals("uuid", user.getUuid());
        assertEquals("username", user.getUsername());
        assertEquals("email", user.getEmail());
        assertEquals("firstName", user.getFirstName());
        assertEquals("lastName", user.getLastName());
    }

}