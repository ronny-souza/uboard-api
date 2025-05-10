package br.com.uboard.core.repository;

import br.com.uboard.core.model.Organization;
import br.com.uboard.exception.OrganizationNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

    Optional<Organization> findByUuidAndUserUuid(String uuid, String userIdentifier);

    default Organization getOrganizationByUuidAndUser(String uuid, String userIdentifier) throws OrganizationNotFoundException {
        return this.findByUuidAndUserUuid(uuid, userIdentifier)
                .orElseThrow(() -> new OrganizationNotFoundException(String.format("Organization %s is not found", uuid)));
    }
}
