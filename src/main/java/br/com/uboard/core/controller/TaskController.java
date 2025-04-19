package br.com.uboard.core.controller;

import br.com.uboard.core.model.filters.TaskFiltersDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.model.transport.TaskDTO;
import br.com.uboard.core.model.transport.TaskProgressDTO;
import br.com.uboard.core.service.GetTaskService;
import br.com.uboard.core.service.ListTasksService;
import br.com.uboard.exception.TaskNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final ListTasksService listTasksService;
    private final GetTaskService getTaskService;

    public TaskController(ListTasksService listTasksService,
                          GetTaskService getTaskService) {
        this.listTasksService = listTasksService;
        this.getTaskService = getTaskService;
    }

    @GetMapping
    public ResponseEntity<Page<TaskDTO>> listTasksAsPage(@AuthenticationPrincipal Jwt jwt,
                                                         @PageableDefault(direction = Sort.Direction.DESC, sort = {"createdAt"}) Pageable pageable,
                                                         TaskFiltersDTO filters) {
        SessionUserDTO sessionUserDTO = new SessionUserDTO(jwt);
        Page<TaskDTO> tasksPage = this.listTasksService.listTasksAsPage(pageable, sessionUserDTO, filters);
        return ResponseEntity.ok(tasksPage);
    }

    @GetMapping("/{uuid}/progress")
    public ResponseEntity<TaskProgressDTO> getTaskProgress(@PathVariable String uuid) throws TaskNotFoundException {
        return ResponseEntity.ok(this.getTaskService.getTaskProgress(uuid));
    }
}
