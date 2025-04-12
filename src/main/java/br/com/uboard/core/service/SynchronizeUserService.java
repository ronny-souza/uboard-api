package br.com.uboard.core.service;

import br.com.uboard.core.model.User;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SynchronizeUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizeUserService.class);
    private final UserRepository userRepository;

    public SynchronizeUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void synchronizeUser(SessionUserDTO sessionUserDTO) {
        this.userRepository.findByUuid(sessionUserDTO.id()).ifPresentOrElse(user -> {
            LOGGER.debug("User is already registered. Updating information...");
            user.setUsername(sessionUserDTO.username() != null ? sessionUserDTO.username() : user.getUsername());
            user.setEmail(sessionUserDTO.email() != null ? sessionUserDTO.email() : user.getEmail());
            user.setFirstName(sessionUserDTO.firstName() != null ? sessionUserDTO.firstName() : user.getFirstName());
            user.setLastName(sessionUserDTO.lastName() != null ? sessionUserDTO.lastName() : user.getLastName());
        }, () -> this.userRepository.save(new User(sessionUserDTO)));
    }
}
