package br.com.uboard.websockets;

import br.com.uboard.core.model.operations.ScrumPokerWebSocketVoteForm;
import br.com.uboard.core.model.transport.ScrumPokerVoteDTO;
import br.com.uboard.core.service.ChangeScrumPokerRoomVotesVisibilityService;
import br.com.uboard.core.service.CloseScrumPokerRoomService;
import br.com.uboard.core.service.DeleteScrumPokerRoomUserService;
import br.com.uboard.core.service.PersistScrumPokerVoteService;
import br.com.uboard.exception.ScrumPokerRoomNotFoundException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrumPokerWebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PersistScrumPokerVoteService persistScrumPokerVoteService;
    private final ChangeScrumPokerRoomVotesVisibilityService changeScrumPokerRoomVotesVisibilityService;
    private final DeleteScrumPokerRoomUserService deleteScrumPokerRoomUserService;
    private final CloseScrumPokerRoomService closeScrumPokerRoomService;

    public ScrumPokerWebSocketController(SimpMessagingTemplate simpMessagingTemplate,
                                         PersistScrumPokerVoteService persistScrumPokerVoteService,
                                         ChangeScrumPokerRoomVotesVisibilityService changeScrumPokerRoomVotesVisibilityService,
                                         DeleteScrumPokerRoomUserService deleteScrumPokerRoomUserService,
                                         CloseScrumPokerRoomService closeScrumPokerRoomService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.persistScrumPokerVoteService = persistScrumPokerVoteService;
        this.changeScrumPokerRoomVotesVisibilityService = changeScrumPokerRoomVotesVisibilityService;
        this.deleteScrumPokerRoomUserService = deleteScrumPokerRoomUserService;
        this.closeScrumPokerRoomService = closeScrumPokerRoomService;
    }

    @MessageMapping("/poker-room/{id}/vote")
    public void submitScrumPokerRoomUserVote(@DestinationVariable String id, ScrumPokerWebSocketVoteForm form) {
        ScrumPokerVoteDTO scrumPokerVoteDTO = this.persistScrumPokerVoteService.persistVote(form);
        this.simpMessagingTemplate.convertAndSend(String.format("/poker-room/%s/votes", id), scrumPokerVoteDTO);
    }

    @MessageMapping("/poker-room/{id}/toggle-votes")
    public void changeScrumPokerRoomVotesVisibility(@DestinationVariable String id, boolean isRoomVotesVisible) {
        this.changeScrumPokerRoomVotesVisibilityService.changeScrumPokerRoomVotesVisibility(id, isRoomVotesVisible);
        this.simpMessagingTemplate.convertAndSend(String.format("/poker-room/%s/votes-visibility", id), isRoomVotesVisible);
    }

    @MessageMapping("/poker-room/{roomIdentifier}/user/{userIdentifier}")
    public void deleteUserFromScrumPokerRoom(@DestinationVariable String roomIdentifier, @DestinationVariable String userIdentifier) {
        this.deleteScrumPokerRoomUserService.deleteUserVote(roomIdentifier, userIdentifier);
        this.simpMessagingTemplate.convertAndSend(String.format("/poker-room/%s/user-left", roomIdentifier), userIdentifier);
    }

    @MessageMapping("/poker-room/{id}/close")
    public void closeScrumPokerRoom(@DestinationVariable String id, String userIdentifier) throws ScrumPokerRoomNotFoundException {
        this.closeScrumPokerRoomService.closeScrumPokerRoom(id, userIdentifier);
        this.simpMessagingTemplate.convertAndSend(String.format("/poker-room/%s/is-closed", id), true);
    }
}
