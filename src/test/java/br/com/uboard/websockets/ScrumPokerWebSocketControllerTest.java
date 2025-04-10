package br.com.uboard.websockets;

import br.com.uboard.core.model.operations.ScrumPokerWebSocketVoteForm;
import br.com.uboard.core.model.transport.ScrumPokerVoteDTO;
import br.com.uboard.core.service.ChangeScrumPokerRoomVotesVisibilityService;
import br.com.uboard.core.service.CloseScrumPokerRoomService;
import br.com.uboard.core.service.DeleteScrumPokerRoomUserService;
import br.com.uboard.core.service.PersistScrumPokerVoteService;
import br.com.uboard.exception.ScrumPokerRoomNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScrumPokerWebSocketControllerTest {

    @InjectMocks
    private ScrumPokerWebSocketController scrumPokerWebSocketController;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private PersistScrumPokerVoteService persistScrumPokerVoteService;

    @Mock
    private ChangeScrumPokerRoomVotesVisibilityService changeScrumPokerRoomVotesVisibilityService;

    @Mock
    private DeleteScrumPokerRoomUserService deleteScrumPokerRoomUserService;

    @Mock
    private CloseScrumPokerRoomService closeScrumPokerRoomService;

    @Test
    @DisplayName("Should persist the vote and emit the event on the WebSocket")
    void shouldPersistVoteAndEmitEventOnTheWebSocket() {
        String id = UUID.randomUUID().toString();
        ScrumPokerWebSocketVoteForm formAsMock = mock(ScrumPokerWebSocketVoteForm.class);
        ScrumPokerVoteDTO scrumPokerVoteDTOAsMock = mock(ScrumPokerVoteDTO.class);

        when(this.persistScrumPokerVoteService.persistVote(formAsMock)).thenReturn(scrumPokerVoteDTOAsMock);

        this.scrumPokerWebSocketController.submitScrumPokerRoomUserVote(id, formAsMock);

        verify(this.simpMessagingTemplate).convertAndSend(String.format("/poker-room/%s/votes", id), scrumPokerVoteDTOAsMock);
    }

    @Test
    @DisplayName("Should change the visibility of votes and emit the event on the WebSocket")
    void shouldChangeVisibilityOfVotesAndEmitTheEventOnTheWebSocket() {
        String id = UUID.randomUUID().toString();
        boolean isRoomVotesVisible = false;

        this.scrumPokerWebSocketController.changeScrumPokerRoomVotesVisibility(id, isRoomVotesVisible);

        verify(this.simpMessagingTemplate).convertAndSend(String.format("/poker-room/%s/votes-visibility", id), isRoomVotesVisible);
    }

    @Test
    @DisplayName("Should remove the user from the room and emit the event on the WebSocket")
    void shouldRemoveUserFromRoomAndEmitEventOnTheWebSocket() {
        String id = UUID.randomUUID().toString();
        String userIdentifier = UUID.randomUUID().toString();

        this.scrumPokerWebSocketController.deleteUserFromScrumPokerRoom(id, userIdentifier);

        verify(this.simpMessagingTemplate).convertAndSend(String.format("/poker-room/%s/user-left", id), userIdentifier);
    }

    @Test
    @DisplayName("Should close the room and emit the event on the WebSocket")
    void shouldCloseTheRoomAndEmitEventOnTheWebSocket() throws ScrumPokerRoomNotFoundException {
        String id = UUID.randomUUID().toString();
        String userIdentifier = UUID.randomUUID().toString();

        this.scrumPokerWebSocketController.closeScrumPokerRoom(id, userIdentifier);
        verify(this.simpMessagingTemplate).convertAndSend(String.format("/poker-room/%s/is-closed", id), true);
    }
}