package br.com.uboard.core.repository.specification;

import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.enums.ScopeEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class OrganizationSpecification {

    private OrganizationSpecification() {

    }

    public static Specification<Organization> belongsToUser(String uuid) {
        return (root, query, criteriaBuilder) ->
                uuid == null ? null : criteriaBuilder.equal(root.get("user").get("uuid"), uuid);
    }

    public static Specification<Organization> containsName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Organization> containsProviderName(String providerName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.join("organizationIntegration").get("providerName")),
                        "%" + providerName.toLowerCase() + "%"
                );
    }

    public static Specification<Organization> hasType(ProviderEnum type) {
        return (root, query, criteriaBuilder) ->
                type == null ? null : criteriaBuilder.equal(
                        root.join("organizationIntegration").get("type"), type
                );
    }

    public static Specification<Organization> hasScope(ScopeEnum scope) {
        return (root, query, criteriaBuilder) ->
                scope == null ? null : criteriaBuilder.equal(
                        root.join("organizationIntegration").get("scope"), scope
                );
    }

    public static Specification<Organization> distinct() {
        return (root, query, criteriaBuilder) -> {
            Objects.requireNonNull(query).distinct(true);
            return null;
        };
    }
}
