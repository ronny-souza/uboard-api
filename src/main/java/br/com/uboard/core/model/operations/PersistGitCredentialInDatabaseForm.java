package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.ProviderEnum;

public record PersistGitCredentialInDatabaseForm(String uuid,
                                                 String name,
                                                 String url,
                                                 ProviderEnum type,
                                                 String userIdentifier) {
}
