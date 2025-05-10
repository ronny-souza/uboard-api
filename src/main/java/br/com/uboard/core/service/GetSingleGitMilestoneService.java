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
import br.com.uboard.exception.*;
import br.com.uboard.factory.GitClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetSingleGitMilestoneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetSingleGitMilestoneService.class);
    private final VaultService vaultService;
    private final GitClientFactory gitClientFactory;
    private final CustomObjectMapper customObjectMapper;

    public GetSingleGitMilestoneService(VaultService vaultService,
                                        GitClientFactory gitClientFactory,
                                        CustomObjectMapper customObjectMapper) {
        this.vaultService = vaultService;
        this.gitClientFactory = gitClientFactory;
        this.customObjectMapper = customObjectMapper;
    }

    public GitMilestoneInterface getSingleMilestone(Long milestoneId,
                                                    Organization organization,
                                                    SessionUserDTO sessionUserDTO) throws UboardApplicationException {
        OrganizationIntegration organizationIntegration = organization.getOrganizationIntegration();
        Credential credential = organization.getCredential();

        Map<String, Object> credentialValueAsMap = this.vaultService.getSecretInVaultService(String.format(
                        VaultService.CREDENTIAL_PATH_PATTERN, sessionUserDTO.id(), credential.getUuid()
                )
        ).orElseThrow(() -> new CredentialNotFoundException("Git credential is not found in Vault"));

        return this.getSingleMilestone(
                credential.getUrl(),
                (String) credentialValueAsMap.get("token"),
                organizationIntegration.getType(),
                organizationIntegration.getScope(),
                String.valueOf(organizationIntegration.getProviderId()),
                String.valueOf(milestoneId)
        );
    }

    private GitMilestoneInterface getSingleMilestone(String url,
                                                     String token,
                                                     ProviderEnum provider,
                                                     ScopeEnum scope,
                                                     String providerId,
                                                     String milestoneId) throws UboardApplicationException {
        try {
            String uriWithSuffix = this.gitClientFactory.getApiUriAsString(url, provider);
            GitClientService client = this.gitClientFactory.getClient(provider, uriWithSuffix);

            Map<String, String> defaultRequestHeaders = this.gitClientFactory.getDefaultRequestHeaders(token, provider);

            LOGGER.debug("Initiating request to base address {} with {} headers...", uriWithSuffix, defaultRequestHeaders.size());
            ResponseEntity<String> response = scope.equals(ScopeEnum.GROUP) ?
                    client.getSingleGroupMilestone(providerId, milestoneId, defaultRequestHeaders) :
                    client.getSingleProjectMilestone(providerId, milestoneId, defaultRequestHeaders);
            if (response == null || response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
                LOGGER.error("No projects found in provider...");
                throw new MilestoneNotFoundException(String.format("Milestone %s not found", milestoneId));
            }

            Class<? extends GitMilestoneInterface> classType = switch (provider) {
                case GITLAB -> GitlabMilestoneDTO.class;
                default -> throw new IllegalArgumentException("There is no type to convert response body");
            };

            return this.customObjectMapper.fromJson(
                    response.getBody(), classType
            );
        } catch (UboardJsonProcessingException e) {
            throw new InternalProcessingException("There is an internal error getting user on provider");
        }
    }
}
