package br.com.uboard.core.controller;

import br.com.uboard.core.model.enums.TaskOperationEnum;
import br.com.uboard.core.model.external.GitGroupInterface;
import br.com.uboard.core.model.external.GitProjectInterface;
import br.com.uboard.core.model.filters.CredentialFiltersDTO;
import br.com.uboard.core.model.operations.CreateCredentialForm;
import br.com.uboard.core.model.operations.CreateTaskForm;
import br.com.uboard.core.model.transport.CredentialDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.model.transport.TaskDTO;
import br.com.uboard.core.service.CreateTaskService;
import br.com.uboard.core.service.ListCredentialsService;
import br.com.uboard.core.service.ListGitGroupsService;
import br.com.uboard.core.service.ListGitProjectsService;
import br.com.uboard.exception.CreateTaskException;
import br.com.uboard.exception.UboardApplicationException;
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

import java.util.List;

@RestController
@RequestMapping("/credentials")
public class CredentialController {
    private final CreateTaskService createTaskService;
    private final ListCredentialsService listCredentialsService;
    private final ListGitProjectsService listGitProjectsService;
    private final ListGitGroupsService listGitGroupsService;

    public CredentialController(CreateTaskService createTaskService,
                                ListCredentialsService listCredentialsService,
                                ListGitProjectsService listGitProjectsService,
                                ListGitGroupsService listGitGroupsService) {
        this.createTaskService = createTaskService;
        this.listCredentialsService = listCredentialsService;
        this.listGitProjectsService = listGitProjectsService;
        this.listGitGroupsService = listGitGroupsService;
    }

    @GetMapping
    public ResponseEntity<Page<CredentialDTO>> listCredentialsAsPage(@AuthenticationPrincipal Jwt jwt,
                                                                     @PageableDefault(direction = Sort.Direction.DESC, sort = {"name"}) Pageable pageable,
                                                                     CredentialFiltersDTO filters) {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        Page<CredentialDTO> credentialsPage = this.listCredentialsService.listCredentialsAsPage(
                pageable, sessionUserDTO, filters
        );
        return ResponseEntity.ok(credentialsPage);
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<List<GitProjectInterface>> listProjects(@AuthenticationPrincipal Jwt jwt,
                                                                  @PathVariable String id,
                                                                  @RequestParam(required = false, name = "page_size", defaultValue = "500") String pageSize)
            throws UboardApplicationException {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        return ResponseEntity.ok(this.listGitProjectsService.listGitProjects(id, sessionUserDTO, pageSize));
    }

    @GetMapping("/{id}/groups")
    public ResponseEntity<List<GitGroupInterface>> listGroups(@AuthenticationPrincipal Jwt jwt,
                                                              @PathVariable String id,
                                                              @RequestParam(required = false, name = "page_size", defaultValue = "500") String pageSize)
            throws UboardApplicationException {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        List<GitGroupInterface> response = this.listGitGroupsService.listGitGroups(id, sessionUserDTO, pageSize);
        return ResponseEntity.ok(response);
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
                .path("/credentials/{id}")
                .buildAndExpand(response.uuid())
                .toUri()
        ).body(response);
    }
}
