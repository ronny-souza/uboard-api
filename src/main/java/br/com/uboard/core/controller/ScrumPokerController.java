package br.com.uboard.core.controller;

import br.com.uboard.core.model.operations.CreateScrumPokerRoomForm;
import br.com.uboard.core.model.transport.ScrumPokerRoomDTO;
import br.com.uboard.core.model.transport.ScrumPokerVoteDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.service.CreateScrumPokerRoomService;
import br.com.uboard.core.service.GetScrumPokerRoomService;
import br.com.uboard.core.service.ListScrumPokerRoomVotesService;
import br.com.uboard.exception.UboardApplicationException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/scrum-poker")
public class ScrumPokerController {
    private final CreateScrumPokerRoomService createScrumPokerRoomService;
    private final GetScrumPokerRoomService getScrumPokerRoomService;
    private final ListScrumPokerRoomVotesService listScrumPokerRoomVotesService;

    public ScrumPokerController(CreateScrumPokerRoomService createScrumPokerRoomService,
                                GetScrumPokerRoomService getScrumPokerRoomService,
                                ListScrumPokerRoomVotesService listScrumPokerRoomVotesService) {
        this.createScrumPokerRoomService = createScrumPokerRoomService;
        this.getScrumPokerRoomService = getScrumPokerRoomService;
        this.listScrumPokerRoomVotesService = listScrumPokerRoomVotesService;
    }

    @PostMapping("/room")
    public ResponseEntity<ScrumPokerRoomDTO> createScrumPokerRoom(@AuthenticationPrincipal Jwt jwt,
                                                                  @RequestBody @Valid CreateScrumPokerRoomForm form,
                                                                  UriComponentsBuilder uriComponentsBuilder)
            throws UboardApplicationException {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        ScrumPokerRoomDTO response = this.createScrumPokerRoomService.createScrumPokerRoom(form, sessionUserDTO);
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
}
