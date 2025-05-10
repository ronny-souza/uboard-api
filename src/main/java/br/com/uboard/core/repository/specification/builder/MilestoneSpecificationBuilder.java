package br.com.uboard.core.repository.specification.builder;

import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.filters.MilestoneFiltersDTO;
import br.com.uboard.core.repository.specification.MilestoneSpecification;
import org.springframework.data.jpa.domain.Specification;

public class MilestoneSpecificationBuilder {

    private Specification<Milestone> specification;

    private MilestoneSpecificationBuilder(Specification<Milestone> specification) {
        this.specification = Specification.where(specification);
    }

    public static MilestoneSpecificationBuilder builder() {
        return new MilestoneSpecificationBuilder(MilestoneSpecification.distinct());
    }

    public MilestoneSpecificationBuilder withSpecification(Specification<Milestone> specification) {
        if (this.specification == null) {
            this.specification = Specification.where(specification);
        } else {
            this.specification = this.specification.and(specification);
        }
        return this;
    }

    public MilestoneSpecificationBuilder withFilters(MilestoneFiltersDTO filters) {
        if (filters == null) {
            return this;
        }

        if (filters.title() != null) {
            this.specification = this.specification.and(MilestoneSpecification.containsTitle(filters.title()));
        }

        if (filters.state() != null) {
            this.specification = this.specification.and(MilestoneSpecification.hasState(filters.state()));
        }

        return this;
    }

    public Specification<Milestone> build() {
        return this.specification;
    }
}
