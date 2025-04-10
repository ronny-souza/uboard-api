package br.com.uboard.core.repository;

import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskStageRepository extends JpaRepository<TaskStage, Long> {

    @Query("SELECT COUNT(*) FROM TaskStage ts WHERE ts.status = :status AND ts.task.uuid = :uuid")
    Long countByStatusAndTask(@Param("status") TaskStatusEnum status, @Param("uuid") String uuid);

    @Query("SELECT COUNT(*) = COUNT(CASE WHEN ts.status = :status THEN 1 END) FROM TaskStage ts WHERE ts.task.uuid = :uuid")
    Boolean allStagesOfTaskAreInCertainState(@Param("status") TaskStatusEnum status, @Param("uuid") String uuid);

    @Query("FROM TaskStage ts WHERE ts.priority = :priority AND ts.task.uuid = :uuid")
    List<TaskStage> findByPriorityAndTask(@Param("priority") Integer priority, @Param("uuid") String uuid);

    List<TaskStage> findByTask(Task task);

    Optional<TaskStage> findByUuid(String uuid);
}
