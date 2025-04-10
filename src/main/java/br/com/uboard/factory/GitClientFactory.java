package br.com.uboard.factory;

import br.com.uboard.client.GitClientService;
import br.com.uboard.client.gitlab.GitlabClientService;
import br.com.uboard.core.model.enums.GitProviderEnum;
import br.com.uboard.exception.GitClientNotFoundException;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Import(FeignClientsConfiguration.class)
public class GitClientFactory {

    private final String gitlabDefaultUrl;
    private final String gitlabApiSuffix;
    private final Feign.Builder feignBuilder;

    @Autowired
    public GitClientFactory(@Value("${uboard.external.gitlab.url}") String gitlabDefaultUrl,
                            @Value("${uboard.external.gitlab.apiSuffix}") String gitlabApiSuffix,
                            Feign.Builder feignBuilder) {
        this.gitlabDefaultUrl = gitlabDefaultUrl;
        this.gitlabApiSuffix = gitlabApiSuffix;
        this.feignBuilder = feignBuilder;
    }

    public GitClientService getClient(GitProviderEnum provider, String url) throws GitClientNotFoundException {
        Class<? extends GitClientService> clientType = switch (provider) {
            case GITLAB -> GitlabClientService.class;
            default -> throw new GitClientNotFoundException(
                    String.format("There is no client configured for the option %s", provider.name())
            );
        };

        return this.feignBuilder.target(clientType, url);
    }

    public String getApiUriAsString(String url, GitProviderEnum provider) {
        return switch (provider) {
            case GITLAB -> StringUtils.hasText(url) ? this.getGitlabApiUrl(url) : this.getGitlabDefaultApiUrl();
            case GITHUB -> "https://api.github.com";
        };
    }

    public Map<String, String> getDefaultHeaders(String token, GitProviderEnum provider) {
        if (provider.equals(GitProviderEnum.GITLAB)) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            headers.put("Accept", MediaType.APPLICATION_JSON_VALUE);
            return headers;
        }

        return new HashMap<>();
    }

    public String getGitlabDefaultApiUrl() {
        return this.gitlabDefaultUrl + this.gitlabApiSuffix;
    }

    public String getGitlabApiUrl(String url) {
        return url + this.gitlabApiSuffix;
    }
}
