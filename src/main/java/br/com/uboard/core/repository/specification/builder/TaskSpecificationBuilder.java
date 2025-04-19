package br.com.uboard.core.repository.specification.builder;

import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.filters.TaskFiltersDTO;
import br.com.uboard.core.repository.specification.TaskSpecification;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecificationBuilder {

    private Specification<Task> specification;

    private TaskSpecificationBuilder(Specification<Task> specification) {
        this.specification = Specification.where(specification);
    }

    public static TaskSpecificationBuilder builder() {
        return new TaskSpecificationBuilder(TaskSpecification.distinct());
    }

    public TaskSpecificationBuilder withSpecification(Specification<Task> specification) {
        if (this.specification == null) {
            this.specification = Specification.where(specification);
        } else {
            this.specification = this.specification.and(specification);
        }
        return this;
    }

    public TaskSpecificationBuilder withFilters(TaskFiltersDTO filters) {
        if (filters == null) {
            return this;
        }

        if (filters.status() != null) {
            this.specification = this.specification.and(TaskSpecification.hasStatus(filters.status()));
        }
        return this;
    }

    public Specification<Task> build() {
        return this.specification;
    }
}
