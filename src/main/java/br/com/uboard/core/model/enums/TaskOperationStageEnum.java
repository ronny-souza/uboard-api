package br.com.uboard.core.model.enums;

public enum TaskOperationStageEnum {
    CHECK_GIT_TOKEN_VALIDITY_IN_PROVIDER,
    REGISTER_TOKEN_IN_SECRETS_MANAGER,
    PERSIST_GIT_CREDENTIAL_IN_DATABASE;
}
