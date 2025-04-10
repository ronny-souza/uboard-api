package br.com.uboard.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;
import org.springframework.vault.core.VaultTemplate;

import java.util.Map;

@Service
public class VaultService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaultService.class);
    private final VaultTemplate vaultTemplate;
    private final String defaultVaultPath;

    public VaultService(VaultTemplate vaultTemplate,
                        @Value("${app.vault.default-path}") String defaultVaultPath) {
        this.vaultTemplate = vaultTemplate;
        this.defaultVaultPath = defaultVaultPath;
    }

    public void addSecret(String path, Map<String, Object> data) {
        LOGGER.debug("Registering secret on Vault service...");
        VaultKeyValueOperations operations = this.vaultTemplate.opsForKeyValue(this.defaultVaultPath, KeyValueBackend.KV_2);
        operations.put(path, data);
    }
}
