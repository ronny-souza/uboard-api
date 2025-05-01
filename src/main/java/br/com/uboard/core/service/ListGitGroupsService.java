package br.com.uboard.core.service;

import br.com.uboard.client.GitClientService;
import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.external.GitGroupInterface;
import br.com.uboard.core.model.external.gitlab.GitlabGroupDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.CredentialRepository;
import br.com.uboard.exception.*;
import br.com.uboard.factory.GitClientFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ListGitGroupsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListGitGroupsService.class);
    private final GitClientFactory gitClientFactory;
    private final CustomObjectMapper customObjectMapper;
    private final CredentialRepository credentialRepository;
    private final VaultService vaultService;

    public ListGitGroupsService(GitClientFactory gitClientFactory,
                                CustomObjectMapper customObjectMapper,
                                CredentialRepository credentialRepository,
                                VaultService vaultService) {
        this.gitClientFactory = gitClientFactory;
        this.customObjectMapper = customObjectMapper;
        this.credentialRepository = credentialRepository;
        this.vaultService = vaultService;
    }

    public List<GitGroupInterface> listGitGroups(String credentialIdentifier, SessionUserDTO sessionUserDTO, String pageSize)
            throws UboardApplicationException {
        Credential credential = this.credentialRepository.findByUuidAndUserUuid(credentialIdentifier, sessionUserDTO.id())
                .orElseThrow(() -> new CredentialNotFoundException("Git credential is not found"));

        Map<String, Object> credentialValueAsMap = this.vaultService.getSecretInVaultService(String.format(
                        VaultService.CREDENTIAL_PATH_PATTERN, sessionUserDTO.id(), credentialIdentifier
                )
        ).orElseThrow(() -> new CredentialNotFoundException("Git credential is not found in Vault"));

        return this.listGitGroups(
                credential.getUrl(),
                (String) credentialValueAsMap.get("token"),
                credential.getType(),
                pageSize);
    }

    public List<GitGroupInterface> listGitGroups(String url, String token, ProviderEnum provider, String pageSize)
            throws UboardApplicationException {
        try {
            String uriWithSuffix = this.gitClientFactory.getApiUriAsString(url, provider);
            GitClientService client = this.gitClientFactory.getClient(provider, uriWithSuffix);

            Map<String, String> defaultRequestHeaders = this.gitClientFactory.getDefaultRequestHeaders(token, provider);
            Map<String, Object> defaultRequestParams = this.getDefaultRequestParams(provider, pageSize);

            LOGGER.debug("Initiating request to base address {} with {} headers...", uriWithSuffix, defaultRequestHeaders.size());
            ResponseEntity<String> response = client.listGroups(defaultRequestHeaders, defaultRequestParams);
            if (response == null || response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
                LOGGER.error("No projects found in provider...");
                return new ArrayList<>();
            }

            TypeReference<? extends List<? extends GitGroupInterface>> typeReference = switch (provider) {
                case GITLAB -> new TypeReference<List<GitlabGroupDTO>>() {
                };
                default -> throw new IllegalArgumentException("There is no type to convert response body");
            };

            List<? extends GitGroupInterface> responseBody = this.customObjectMapper.fromJson(
                    response.getBody(), typeReference
            );

            return new ArrayList<>(responseBody);
        } catch (UboardJsonProcessingException e) {
            throw new InternalProcessingException("There is an internal error getting user on provider");
        }
    }

    private Map<String, Object> getDefaultRequestParams(ProviderEnum provider, String pageSize) {
        if (provider.equals(ProviderEnum.GITLAB)) {
            Map<String, Object> params = new HashMap<>();
            params.put("per_page", pageSize);
            return params;
        }

        return new HashMap<>();
    }
}
