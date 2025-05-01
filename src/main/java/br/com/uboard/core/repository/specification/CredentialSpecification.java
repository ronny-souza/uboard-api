package br.com.uboard.core.repository.specification;

import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.enums.ProviderEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class CredentialSpecification {

    private CredentialSpecification() {

    }

    public static Specification<Credential> belongsToUser(String uuid) {
        return (root, query, criteriaBuilder) ->
                uuid == null ? null : criteriaBuilder.equal(root.get("user").get("uuid"), uuid);
    }

    public static Specification<Credential> containsName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Credential> containsUrl(String url) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("url")), "%" + url.toLowerCase() + "%");
    }

    public static Specification<Credential> hasType(ProviderEnum type) {
        return (root, query, criteriaBuilder) ->
                type == null ? null : criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<Credential> distinct() {
        return (root, query, criteriaBuilder) -> {
            Objects.requireNonNull(query).distinct(true);
            return null;
        };
    }
}
