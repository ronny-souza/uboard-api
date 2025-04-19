package br.com.uboard.core.repository.specification;

import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class TaskSpecification {

    private TaskSpecification() {

    }

    public static Specification<Task> belongsToUser(String uuid) {
        return (root, query, criteriaBuilder) ->
                uuid == null ? null : criteriaBuilder.equal(root.get("user").get("uuid"), uuid);
    }

    public static Specification<Task> hasStatus(TaskStatusEnum status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Task> distinct() {
        return (root, query, criteriaBuilder) -> {
            Objects.requireNonNull(query).distinct(true);
            return null;
        };
    }
}
