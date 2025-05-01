package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.ProviderEnum;

public record PersistGitCredentialOnDatabaseForm(String uuid,
                                                 String name,
                                                 String url,
                                                 ProviderEnum type,
                                                 String userIdentifier) {
}
