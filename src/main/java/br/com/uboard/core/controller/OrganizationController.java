package br.com.uboard.core.controller;

import br.com.uboard.core.model.enums.TaskOperationEnum;
import br.com.uboard.core.model.filters.OrganizationFiltersDTO;
import br.com.uboard.core.model.operations.CreateOrganizationForm;
import br.com.uboard.core.model.operations.CreateTaskForm;
import br.com.uboard.core.model.transport.OrganizationDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.model.transport.TaskDTO;
import br.com.uboard.core.service.CreateTaskService;
import br.com.uboard.core.service.ListOrganizationsService;
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
@RequestMapping("/organizations")
public class OrganizationController {
    private final CreateTaskService createTaskService;
    private final ListOrganizationsService listOrganizationsService;

    public OrganizationController(CreateTaskService createTaskService,
                                  ListOrganizationsService listOrganizationsService) {
        this.createTaskService = createTaskService;
        this.listOrganizationsService = listOrganizationsService;
    }

    @GetMapping
    public ResponseEntity<Page<OrganizationDTO>> listOrganizationsAsPage(@AuthenticationPrincipal Jwt jwt,
                                                                         @PageableDefault(direction = Sort.Direction.DESC, sort = {"name"}) Pageable pageable,
                                                                         OrganizationFiltersDTO filters) {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        Page<OrganizationDTO> organizationsPage = this.listOrganizationsService.listOrganizationsAsPage(
                pageable, sessionUserDTO, filters
        );
        return ResponseEntity.ok(organizationsPage);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createOrganization(@AuthenticationPrincipal Jwt jwt,
                                                      @RequestBody @Valid CreateOrganizationForm form,
                                                      UriComponentsBuilder uriComponentsBuilder) throws CreateTaskException {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        TaskDTO response = this.createTaskService.create(CreateTaskForm.newInstance(TaskOperationEnum.CREATE_ORGANIZATION,
                        form,
                        form.name(),
                        sessionUserDTO
                )
        );

        return ResponseEntity.created(uriComponentsBuilder
                .path("/organizations/{id}")
                .buildAndExpand(response.uuid())
                .toUri()
        ).body(response);
    }
}
