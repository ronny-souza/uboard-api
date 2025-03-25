package br.com.uboard.core.controller;

import br.com.uboard.core.model.operations.CreateScrumPokerRoomForm;
import br.com.uboard.core.model.transport.ScrumPokerRoomDTO;
import br.com.uboard.core.model.transport.ScrumPokerVoteDTO;
import br.com.uboard.core.service.CreateScrumPokerRoomService;
import br.com.uboard.core.service.DeleteScrumPokerRoomUserService;
import br.com.uboard.core.service.GetScrumPokerRoomService;
import br.com.uboard.core.service.ListScrumPokerRoomVotesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/scrum-poker")
public class ScrumPokerController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CreateScrumPokerRoomService createScrumPokerRoomService;
    private final GetScrumPokerRoomService getScrumPokerRoomService;
    private final ListScrumPokerRoomVotesService listScrumPokerRoomVotesService;
    private final DeleteScrumPokerRoomUserService deleteScrumPokerRoomUserService;

    public ScrumPokerController(SimpMessagingTemplate simpMessagingTemplate, CreateScrumPokerRoomService createScrumPokerRoomService,
                                GetScrumPokerRoomService getScrumPokerRoomService,
                                ListScrumPokerRoomVotesService listScrumPokerRoomVotesService,
                                DeleteScrumPokerRoomUserService deleteScrumPokerRoomUserService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.createScrumPokerRoomService = createScrumPokerRoomService;
        this.getScrumPokerRoomService = getScrumPokerRoomService;
        this.listScrumPokerRoomVotesService = listScrumPokerRoomVotesService;
        this.deleteScrumPokerRoomUserService = deleteScrumPokerRoomUserService;
    }

    @PostMapping("/room")
    public ResponseEntity<ScrumPokerRoomDTO> createScrumPokerRoom(@AuthenticationPrincipal Jwt jwt,
                                                                  @RequestBody @Valid CreateScrumPokerRoomForm form,
                                                                  UriComponentsBuilder uriComponentsBuilder) {
        ScrumPokerRoomDTO response = this.createScrumPokerRoomService.createScrumPokerRoom(form);
        return ResponseEntity.created(uriComponentsBuilder
                .path("/scrum-poker/{id}")
                .buildAndExpand(response.uuid())
                .toUri()
        ).body(response);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<ScrumPokerRoomDTO> getScrumPokerRoom(@PathVariable String id) {
        return ResponseEntity.ok(this.getScrumPokerRoomService.getScrumPokerRoom(id));
    }

    @GetMapping("/room/{id}/votes")
    public ResponseEntity<List<ScrumPokerVoteDTO>> listScrumPokerVotes(@PathVariable String id) {
        return ResponseEntity.ok(this.listScrumPokerRoomVotesService.listScrumPokerRoomVotes(id));
    }

    @DeleteMapping("/room/{roomIdentifier}/user/{userIdentifier}")
    public ResponseEntity<Void> deleteUserFromScrumPokerRoom(@PathVariable String roomIdentifier,
                                                             @PathVariable String userIdentifier) {
        this.deleteScrumPokerRoomUserService.deleteUserVote(roomIdentifier, userIdentifier);
        this.simpMessagingTemplate.convertAndSend(
                String.format("/poker-room/%s/user-left", roomIdentifier),
                userIdentifier
        );
        return ResponseEntity.noContent().build();
    }
}
