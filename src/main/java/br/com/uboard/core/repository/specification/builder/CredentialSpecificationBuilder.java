package br.com.uboard.core.repository.specification.builder;

import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.filters.CredentialFiltersDTO;
import br.com.uboard.core.repository.specification.CredentialSpecification;
import org.springframework.data.jpa.domain.Specification;

public class CredentialSpecificationBuilder {

    private Specification<Credential> specification;

    private CredentialSpecificationBuilder(Specification<Credential> specification) {
        this.specification = Specification.where(specification);
    }

    public static CredentialSpecificationBuilder builder() {
        return new CredentialSpecificationBuilder(CredentialSpecification.distinct());
    }

    public CredentialSpecificationBuilder withSpecification(Specification<Credential> specification) {
        if (this.specification == null) {
            this.specification = Specification.where(specification);
        } else {
            this.specification = this.specification.and(specification);
        }
        return this;
    }

    public CredentialSpecificationBuilder withFilters(CredentialFiltersDTO filters) {
        if (filters == null) {
            return this;
        }

        if (filters.name() != null) {
            this.specification = this.specification.and(CredentialSpecification.containsName(filters.name()));
        }

        if (filters.url() != null) {
            this.specification = this.specification.and(CredentialSpecification.containsUrl(filters.url()));
        }

        if (filters.type() != null) {
            this.specification = this.specification.and(CredentialSpecification.hasType(filters.type()));
        }

        return this;
    }

    public Specification<Credential> build() {
        return this.specification;
    }
}
