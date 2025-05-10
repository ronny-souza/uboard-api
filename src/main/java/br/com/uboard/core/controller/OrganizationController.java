package br.com.uboard.core.controller;

import br.com.uboard.core.model.enums.TaskOperationEnum;
import br.com.uboard.core.model.external.GitMilestoneInterface;
import br.com.uboard.core.model.filters.MilestoneFiltersDTO;
import br.com.uboard.core.model.filters.OrganizationFiltersDTO;
import br.com.uboard.core.model.operations.CreateOrganizationForm;
import br.com.uboard.core.model.operations.CreateTaskForm;
import br.com.uboard.core.model.operations.SynchronizeMilestoneForm;
import br.com.uboard.core.model.transport.MilestoneDTO;
import br.com.uboard.core.model.transport.OrganizationDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.model.transport.TaskDTO;
import br.com.uboard.core.service.*;
import br.com.uboard.exception.CreateTaskException;
import br.com.uboard.exception.OrganizationNotFoundException;
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
@RequestMapping("/organizations")
public class OrganizationController {
    private final CreateTaskService createTaskService;
    private final GetOrganizationService getOrganizationService;
    private final ListOrganizationsService listOrganizationsService;
    private final ListGitMilestonesService listGitMilestonesService;
    private final ListOrganizationMilestonesService listOrganizationMilestonesService;

    public OrganizationController(CreateTaskService createTaskService,
                                  GetOrganizationService getOrganizationService,
                                  ListOrganizationsService listOrganizationsService,
                                  ListGitMilestonesService listGitMilestonesService,
                                  ListOrganizationMilestonesService listOrganizationMilestonesService) {
        this.createTaskService = createTaskService;
        this.getOrganizationService = getOrganizationService;
        this.listOrganizationsService = listOrganizationsService;
        this.listGitMilestonesService = listGitMilestonesService;
        this.listOrganizationMilestonesService = listOrganizationMilestonesService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable String id,
                                                           @AuthenticationPrincipal Jwt jwt) throws OrganizationNotFoundException {
        return ResponseEntity.ok(this.getOrganizationService.getOrganization(id, new SessionUserDTO(jwt)));
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

    @GetMapping("/{id}/milestones")
    public ResponseEntity<Page<MilestoneDTO>> listOrganizationMilestonesAsPage(@PathVariable String id,
                                                                               @AuthenticationPrincipal Jwt jwt,
                                                                               @PageableDefault(direction = Sort.Direction.DESC, sort = {"title"}) Pageable pageable,
                                                                               MilestoneFiltersDTO filters) {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        Page<MilestoneDTO> milestonesPage = this.listOrganizationMilestonesService.listMilestonesAsPage(
                pageable, sessionUserDTO, id, filters
        );
        return ResponseEntity.ok(milestonesPage);
    }

    @GetMapping("/{id}/discover-milestones")
    public ResponseEntity<List<GitMilestoneInterface>> discoverMilestones(@AuthenticationPrincipal Jwt jwt,
                                                                          @PathVariable String id,
                                                                          @RequestParam(required = false, name = "page_size", defaultValue = "500") String pageSize)
            throws UboardApplicationException {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        List<GitMilestoneInterface> response = this.listGitMilestonesService.listMilestones(id, sessionUserDTO, pageSize);
        return ResponseEntity.ok(response);
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

    @PostMapping("/{id}/synchronize-milestone")
    public ResponseEntity<TaskDTO> synchronizeMilestone(@AuthenticationPrincipal Jwt jwt,
                                                        @PathVariable String id,
                                                        @RequestBody @Valid SynchronizeMilestoneForm form,
                                                        UriComponentsBuilder uriComponentsBuilder) throws CreateTaskException {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        SynchronizeMilestoneForm formWithOrganizationIdentifier = new SynchronizeMilestoneForm(
                id, form.milestone(), form.isAutoSync(), form.isImporting(), form.frequency(), form.hours(), form.minutes(), form.weekDay()
        );

        TaskOperationEnum operation = form.isImporting() ? TaskOperationEnum.IMPORT_MILESTONE : TaskOperationEnum.SYNCHRONIZE_MILESTONE;
        TaskDTO response = this.createTaskService.create(CreateTaskForm.newInstance(operation,
                        formWithOrganizationIdentifier,
                        formWithOrganizationIdentifier.milestone().title(),
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
