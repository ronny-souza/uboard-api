package br.com.uboard.core.repository;

import br.com.uboard.core.model.User;
import br.com.uboard.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(String uuid);

    default User getUserByUuid(String uuid) throws UserNotFoundException {
        return this.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException("User is not found"));
    }
}
