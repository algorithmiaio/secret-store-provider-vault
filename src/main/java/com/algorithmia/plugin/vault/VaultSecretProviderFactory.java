package com.algorithmia.plugin.vault;

import com.algorithmia.sdk.plugin.secrets.SecretProvider;
import com.algorithmia.sdk.plugin.secrets.SecretProviderFactory;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.Vault;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public class VaultSecretProviderFactory implements SecretProviderFactory {


    /**
     * After this returns, the database has been connected to and tables created as needed.
     */
    @Override
    public SecretProvider create(Map<String, String> config) {
      require(config, "vault_addr"); 
      require(config, "vault_token");
      require(config, "vault_secret_path");

      try
      {
        VaultConfig vault_config = new VaultConfig()
          .address(config.get("vault_addr"))
          .token(config.get("vault_token"))
          .build();

        Vault vault = new Vault(vault_config);

        String secret_path = config.get("vault_secret_path");

        return new VaultSecretProvider(vault, secret_path);
      }
      catch(com.bettercloud.vault.VaultException e)
      {
        throw new RuntimeException(e);
      }
    }

    private void require(Map<String, String> config, String field)
    {
      if (!config.containsKey(field)) throw new RuntimeException("VaultSecretProviderFactory Missing required config field: " + field);
    }
}
