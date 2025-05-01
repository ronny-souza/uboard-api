package br.com.uboard.core.service;

import br.com.uboard.client.GitClientService;
import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.external.GitUserInterface;
import br.com.uboard.core.model.external.gitlab.GitlabUserDTO;
import br.com.uboard.exception.GitClientNotFoundException;
import br.com.uboard.exception.GitUserNotFoundException;
import br.com.uboard.exception.InternalProcessingException;
import br.com.uboard.exception.UboardJsonProcessingException;
import br.com.uboard.factory.GitClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetCurrentGitUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetCurrentGitUserService.class);
    private final GitClientFactory gitClientFactory;
    private final CustomObjectMapper customObjectMapper;

    public GetCurrentGitUserService(GitClientFactory gitClientFactory,
                                    CustomObjectMapper customObjectMapper) {
        this.gitClientFactory = gitClientFactory;
        this.customObjectMapper = customObjectMapper;
    }

    public GitUserInterface getCurrentUser(String url, String token, ProviderEnum provider)
            throws GitClientNotFoundException, GitUserNotFoundException, InternalProcessingException {
        try {
            String uriWithSuffix = this.gitClientFactory.getApiUriAsString(url, provider);
            GitClientService client = this.gitClientFactory.getClient(provider, uriWithSuffix);

            Map<String, String> defaultHeaders = this.gitClientFactory.getDefaultRequestHeaders(token, provider);

            LOGGER.debug("Initiating request to base address {} with {} headers...", uriWithSuffix, defaultHeaders.size());
            ResponseEntity<String> response = client.getCurrentUser(defaultHeaders);
            if (response == null || response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
                throw new GitUserNotFoundException("User for provided token is not found on git provider");
            }

            Class<? extends GitUserInterface> classType = switch (provider) {
                case GITLAB -> GitlabUserDTO.class;
                default -> throw new IllegalArgumentException("There is no type to convert response body");
            };

            return this.customObjectMapper.fromJson(response.getBody(), classType);
        } catch (UboardJsonProcessingException e) {
            throw new InternalProcessingException("There is an internal error getting user on provider");
        }
    }
}
