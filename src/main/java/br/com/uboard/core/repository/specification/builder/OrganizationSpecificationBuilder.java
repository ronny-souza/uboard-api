package br.com.uboard.core.repository.specification.builder;

import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.filters.OrganizationFiltersDTO;
import br.com.uboard.core.repository.specification.OrganizationSpecification;
import org.springframework.data.jpa.domain.Specification;

public class OrganizationSpecificationBuilder {

    private Specification<Organization> specification;

    private OrganizationSpecificationBuilder(Specification<Organization> specification) {
        this.specification = Specification.where(specification);
    }

    public static OrganizationSpecificationBuilder builder() {
        return new OrganizationSpecificationBuilder(OrganizationSpecification.distinct());
    }

    public OrganizationSpecificationBuilder withSpecification(Specification<Organization> specification) {
        if (this.specification == null) {
            this.specification = Specification.where(specification);
        } else {
            this.specification = this.specification.and(specification);
        }
        return this;
    }

    public OrganizationSpecificationBuilder withFilters(OrganizationFiltersDTO filters) {
        if (filters == null) {
            return this;
        }

        if (filters.name() != null) {
            this.specification = this.specification.and(OrganizationSpecification.containsName(filters.name()));
        }

        if (filters.providerName() != null) {
            this.specification = this.specification.and(OrganizationSpecification.containsProviderName(filters.providerName()));
        }

        if (filters.type() != null) {
            this.specification = this.specification.and(OrganizationSpecification.hasType(filters.type()));
        }

        if (filters.scope() != null) {
            this.specification = this.specification.and(OrganizationSpecification.hasScope(filters.scope()));
        }

        return this;
    }

    public Specification<Organization> build() {
        return this.specification;
    }
}
