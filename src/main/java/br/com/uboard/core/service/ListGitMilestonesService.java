package br.com.uboard.core.service;

import br.com.uboard.client.GitClientService;
import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.OrganizationIntegration;
import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.enums.ScopeEnum;
import br.com.uboard.core.model.external.GitMilestoneInterface;
import br.com.uboard.core.model.external.gitlab.GitlabMilestoneDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.OrganizationRepository;
import br.com.uboard.exception.CredentialNotFoundException;
import br.com.uboard.exception.InternalProcessingException;
import br.com.uboard.exception.UboardApplicationException;
import br.com.uboard.exception.UboardJsonProcessingException;
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
public class ListGitMilestonesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListGitMilestonesService.class);
    private final OrganizationRepository organizationRepository;
    private final VaultService vaultService;
    private final GitClientFactory gitClientFactory;
    private final CustomObjectMapper customObjectMapper;

    public ListGitMilestonesService(OrganizationRepository organizationRepository,
                                    VaultService vaultService,
                                    GitClientFactory gitClientFactory,
                                    CustomObjectMapper customObjectMapper) {
        this.organizationRepository = organizationRepository;
        this.vaultService = vaultService;
        this.gitClientFactory = gitClientFactory;
        this.customObjectMapper = customObjectMapper;
    }

    public List<GitMilestoneInterface> listMilestones(String organizationId, SessionUserDTO sessionUserDTO, String pageSize) throws UboardApplicationException {
        Organization organization = this.organizationRepository.getOrganizationByUuidAndUser(organizationId, sessionUserDTO.id());
        OrganizationIntegration organizationIntegration = organization.getOrganizationIntegration();
        Credential credential = organization.getCredential();

        Map<String, Object> credentialValueAsMap = this.vaultService.getSecretInVaultService(String.format(
                        VaultService.CREDENTIAL_PATH_PATTERN, sessionUserDTO.id(), credential.getUuid()
                )
        ).orElseThrow(() -> new CredentialNotFoundException("Git credential is not found in Vault"));

        return this.listGitMilestones(
                credential.getUrl(),
                (String) credentialValueAsMap.get("token"),
                organizationIntegration.getType(),
                organizationIntegration.getScope(),
                String.valueOf(organizationIntegration.getProviderId()),
                pageSize
        );
    }

    private List<GitMilestoneInterface> listGitMilestones(String url,
                                                          String token,
                                                          ProviderEnum provider,
                                                          ScopeEnum scope,
                                                          String providerId,
                                                          String pageSize) throws UboardApplicationException {
        try {
            String uriWithSuffix = this.gitClientFactory.getApiUriAsString(url, provider);
            GitClientService client = this.gitClientFactory.getClient(provider, uriWithSuffix);

            Map<String, String> defaultRequestHeaders = this.gitClientFactory.getDefaultRequestHeaders(token, provider);
            Map<String, Object> defaultRequestParams = this.getDefaultRequestParams(provider, pageSize);

            LOGGER.debug("Initiating request to base address {} with {} headers...", uriWithSuffix, defaultRequestHeaders.size());
            ResponseEntity<String> response = scope.equals(ScopeEnum.GROUP) ?
                    client.listGroupMilestones(providerId, defaultRequestHeaders, defaultRequestParams) :
                    client.listProjectMilestones(providerId, defaultRequestHeaders, defaultRequestParams);
            if (response == null || response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
                LOGGER.error("No milestones found in provider...");
                return new ArrayList<>();
            }

            TypeReference<? extends List<? extends GitMilestoneInterface>> typeReference = switch (provider) {
                case GITLAB -> new TypeReference<List<GitlabMilestoneDTO>>() {
                };
                default -> throw new IllegalArgumentException("There is no type to convert response body");
            };

            List<? extends GitMilestoneInterface> responseBody = this.customObjectMapper.fromJson(
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
