package br.com.uboard.core.repository;

import br.com.uboard.core.model.Milestone;
import br.com.uboard.exception.MilestoneNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MilestoneRepository extends JpaRepository<Milestone, Long>, JpaSpecificationExecutor<Milestone> {

    Optional<Milestone> findByProviderIdAndOrganizationUuid(Long providerId, String organizationUuid);

    Optional<Milestone> findByUuid(String uuid);

    default Milestone getMilestoneByUuid(String uuid) throws MilestoneNotFoundException {
        return this.findByUuid(uuid)
                .orElseThrow(() -> new MilestoneNotFoundException(String.format("Milestone %s is not found", uuid)));
    }
}
