package br.com.uboard.core.repository;

import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.transport.TaskProgressDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    Optional<Task> findByUuid(String uuid);

    @Query("SELECT t.status AS status, t.progress as progress FROM Task t WHERE t.uuid = :uuid")
    Optional<TaskProgressDTO> getTaskProgress(@Param("uuid") String uuid);
}
