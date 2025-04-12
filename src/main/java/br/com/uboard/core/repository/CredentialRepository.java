package br.com.uboard.core.repository;

import br.com.uboard.core.model.Credential;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Page<Credential> findAllByUserUuid(String uuid, Pageable pageable);
}
