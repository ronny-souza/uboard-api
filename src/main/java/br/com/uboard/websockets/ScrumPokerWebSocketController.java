package br.com.uboard.websockets;

import br.com.uboard.core.model.operations.ScrumPokerWebSocketVoteForm;
import br.com.uboard.core.model.transport.ScrumPokerVoteDTO;
import br.com.uboard.core.service.PersistScrumPokerVoteService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrumPokerWebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PersistScrumPokerVoteService persistScrumPokerVoteService;

    public ScrumPokerWebSocketController(SimpMessagingTemplate simpMessagingTemplate,
                                         PersistScrumPokerVoteService persistScrumPokerVoteService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.persistScrumPokerVoteService = persistScrumPokerVoteService;
    }

    @MessageMapping("/poker-room/{id}/vote")
    public void submitVote(@DestinationVariable String id, ScrumPokerWebSocketVoteForm form) {
        ScrumPokerVoteDTO scrumPokerVoteDTO = this.persistScrumPokerVoteService.persistVote(form);
        this.simpMessagingTemplate.convertAndSend(String.format("/poker-room/%s/votes", id), scrumPokerVoteDTO);
    }
}
