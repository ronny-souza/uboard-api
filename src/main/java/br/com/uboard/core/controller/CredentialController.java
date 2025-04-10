package br.com.uboard.core.controller;

import br.com.uboard.core.model.enums.TaskOperationEnum;
import br.com.uboard.core.model.operations.CreateCredentialForm;
import br.com.uboard.core.model.operations.CreateTaskForm;
import br.com.uboard.core.model.transport.CredentialDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.model.transport.TaskDTO;
import br.com.uboard.core.service.CreateTaskService;
import br.com.uboard.core.service.ListCredentialsService;
import br.com.uboard.exception.CreateTaskException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/credentials")
public class CredentialController {

    private final CreateTaskService createTaskService;
    private final ListCredentialsService listCredentialsService;

    public CredentialController(CreateTaskService createTaskService,
                                ListCredentialsService listCredentialsService) {
        this.createTaskService = createTaskService;
        this.listCredentialsService = listCredentialsService;
    }

    @GetMapping
    public ResponseEntity<Page<CredentialDTO>> listCredentialsAsPage(@AuthenticationPrincipal Jwt jwt,
                                                                     @PageableDefault(direction = Sort.Direction.ASC, sort = {"name"}) Pageable pageable) {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        Page<CredentialDTO> credentialsPage = this.listCredentialsService.listCredentialsAsPage(pageable, sessionUserDTO);
        return ResponseEntity.ok(credentialsPage);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createCredential(@AuthenticationPrincipal Jwt jwt,
                                                    @RequestBody @Valid CreateCredentialForm form,
                                                    UriComponentsBuilder uriComponentsBuilder) throws CreateTaskException {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);

        TaskDTO response = this.createTaskService.create(CreateTaskForm.newInstance(TaskOperationEnum.CREATE_CREDENTIAL,
                        form,
                        form.name(),
                        sessionUserDTO
                )
        );

        return ResponseEntity.created(uriComponentsBuilder
                .path("/tasks/{id}")
                .buildAndExpand(response.uuid())
                .toUri()
        ).body(response);
    }
}
