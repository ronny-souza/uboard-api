package br.com.uboard.core.service;

import br.com.uboard.core.model.transport.TaskProgressDTO;
import br.com.uboard.core.repository.TaskRepository;
import br.com.uboard.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetTaskService {

    private final TaskRepository taskRepository;

    public GetTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskProgressDTO getTaskProgress(String uuid) throws TaskNotFoundException {
        return this.taskRepository.getTaskProgress(uuid)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task %s is not found", uuid)));
    }
}
