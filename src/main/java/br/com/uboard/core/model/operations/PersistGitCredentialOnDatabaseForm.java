package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.GitProviderEnum;

public record PersistGitCredentialOnDatabaseForm(String uuid,
                                                 String name,
                                                 String url,
                                                 GitProviderEnum type,
                                                 String userIdentifier) {
}
