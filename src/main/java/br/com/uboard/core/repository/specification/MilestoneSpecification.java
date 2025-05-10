package br.com.uboard.core.repository.specification;

import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.enums.MilestoneStateEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class MilestoneSpecification {

    private MilestoneSpecification() {

    }

    public static Specification<Milestone> belongsToOrganization(String uuid) {
        return (root, query, criteriaBuilder) ->
                uuid == null ? null : criteriaBuilder.equal(root.get("organization").get("uuid"), uuid);
    }

    public static Specification<Milestone> containsTitle(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Milestone> hasState(MilestoneStateEnum state) {
        return (root, query, criteriaBuilder) ->
                state == null ? null : criteriaBuilder.equal(root.get("state"), state);
    }

    public static Specification<Milestone> distinct() {
        return (root, query, criteriaBuilder) -> {
            Objects.requireNonNull(query).distinct(true);
            return null;
        };
    }
}
