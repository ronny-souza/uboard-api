package br.com.uboard.core.repository;

import br.com.uboard.core.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long>, JpaSpecificationExecutor<Credential> {

    Optional<Credential> findByUuidAndUserUuid(String uuid, String userIdentifier);
}
