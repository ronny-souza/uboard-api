package br.com.uboard.core.service;

import br.com.uboard.client.GitClientService;
import br.com.uboard.common.CustomObjectMapper;
import br.com.uboard.core.model.Credential;
import br.com.uboard.core.model.Organization;
import br.com.uboard.core.model.OrganizationIntegration;
import br.com.uboard.core.model.enums.ProviderEnum;
import br.com.uboard.core.model.enums.ScopeEnum;
import br.com.uboard.core.model.external.GitIssueInterface;
import br.com.uboard.core.model.external.gitlab.GitlabIssueDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
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
public class ListGitMilestoneIssuesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListGitMilestoneIssuesService.class);
    private final GitClientFactory gitClientFactory;
    private final VaultService vaultService;
    private final CustomObjectMapper customObjectMapper;

    public ListGitMilestoneIssuesService(GitClientFactory gitClientFactory,
                                         VaultService vaultService,
                                         CustomObjectMapper customObjectMapper) {
        this.gitClientFactory = gitClientFactory;
        this.vaultService = vaultService;
        this.customObjectMapper = customObjectMapper;
    }

    public List<GitIssueInterface<?>> listMilestoneIssues(Organization organization,
                                                          String milestoneName,
                                                          SessionUserDTO sessionUserDTO) throws UboardApplicationException {
        Credential credential = organization.getCredential();

        Map<String, Object> credentialValueAsMap = this.vaultService.getSecretInVaultService(String.format(
                        VaultService.CREDENTIAL_PATH_PATTERN, sessionUserDTO.id(), credential.getUuid()
                )
        ).orElseThrow(() -> new CredentialNotFoundException("Git credential is not found in Vault"));

        return this.listGitIssues(
                credential.getUrl(),
                (String) credentialValueAsMap.get("token"),
                milestoneName,
                organization
        );
    }

    private List<GitIssueInterface<?>> listGitIssues(String url,
                                                     String token,
                                                     String milestoneName,
                                                     Organization organization) throws UboardApplicationException {
        try {
            OrganizationIntegration organizationIntegration = organization.getOrganizationIntegration();
            ProviderEnum providerType = organizationIntegration.getType();
            ScopeEnum scope = organizationIntegration.getScope();
            String providerId = String.valueOf(organizationIntegration.getProviderId());

            String uriWithSuffix = this.gitClientFactory.getApiUriAsString(url, providerType);
            GitClientService client = this.gitClientFactory.getClient(providerType, uriWithSuffix);

            Map<String, String> defaultRequestHeaders = this.gitClientFactory.getDefaultRequestHeaders(token, providerType);
            Map<String, Object> defaultRequestParams = this.getDefaultRequestParams(providerType, milestoneName);

            LOGGER.debug("Initiating request to base address {} with {} headers...", uriWithSuffix, defaultRequestHeaders.size());
            ResponseEntity<String> response = scope.equals(ScopeEnum.GROUP) ?
                    client.listGroupMilestoneIssues(providerId, defaultRequestHeaders, defaultRequestParams) :
                    client.listProjectMilestoneIssues(providerId, defaultRequestHeaders, defaultRequestParams);
            if (response == null || response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
                LOGGER.error("No milestone issues found in provider...");
                return new ArrayList<>();
            }

            TypeReference<? extends List<? extends GitIssueInterface<?>>> typeReference = switch (providerType) {
                case GITLAB -> new TypeReference<List<GitlabIssueDTO>>() {
                };
                default -> throw new IllegalArgumentException("There is no type to convert response body");
            };

            List<? extends GitIssueInterface<?>> responseBody = this.customObjectMapper.fromJson(
                    response.getBody(), typeReference
            );

            return new ArrayList<>(responseBody);
        } catch (UboardJsonProcessingException e) {
            throw new InternalProcessingException("There is an internal error getting user on provider");
        }
    }

    private Map<String, Object> getDefaultRequestParams(ProviderEnum providerType,
                                                        String milestoneName) {
        if (providerType.equals(ProviderEnum.GITLAB)) {
            Map<String, Object> params = new HashMap<>();
            params.put("per_page", "1000");
            params.put("milestone", milestoneName);
            params.put("scope", "all");
            return params;
        }

        return new HashMap<>();
    }
}
