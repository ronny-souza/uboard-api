package br.com.uboard.core.service;

import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.filters.TaskFiltersDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.model.transport.TaskDTO;
import br.com.uboard.core.repository.TaskRepository;
import br.com.uboard.core.repository.specification.TaskSpecification;
import br.com.uboard.core.repository.specification.builder.TaskSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ListTasksService {

    private final TaskRepository taskRepository;

    public ListTasksService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<TaskDTO> listTasksAsPage(Pageable pageable,
                                         SessionUserDTO sessionUserDTO,
                                         TaskFiltersDTO filters) {
        Specification<Task> specifications = TaskSpecificationBuilder.builder()
                .withSpecification(TaskSpecification.belongsToUser(sessionUserDTO.id()))
                .withFilters(filters)
                .build();
        return this.taskRepository.findAll(specifications, pageable).map(TaskDTO::new);
    }
}
