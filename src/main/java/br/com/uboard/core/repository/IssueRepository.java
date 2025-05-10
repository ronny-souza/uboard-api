package br.com.uboard.core.repository;

import br.com.uboard.core.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {

    Optional<Issue> findByProviderIdAndMilestoneProviderId(Long providerId, Long milestoneProviderId);
}
