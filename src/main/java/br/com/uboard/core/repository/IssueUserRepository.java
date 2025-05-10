package br.com.uboard.core.repository;

import br.com.uboard.core.model.IssueUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssueUserRepository extends JpaRepository<IssueUser, Long> {

    Optional<IssueUser> findByProviderId(Long providerId);
}
