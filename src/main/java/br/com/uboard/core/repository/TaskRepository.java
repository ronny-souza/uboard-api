package br.com.uboard.core.repository;

import br.com.uboard.core.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByUuid(String uuid);
}
