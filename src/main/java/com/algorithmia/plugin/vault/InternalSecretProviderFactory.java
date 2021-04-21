package com.algorithmia.plugin.vault;

import com.algorithmia.sdk.plugin.secrets.SecretProvider;
import com.algorithmia.sdk.plugin.secrets.SecretProviderFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public class VaultSecretProviderFactory implements SecretProviderFactory {


    /**
     * After this returns, the database has been connected to and tables created as needed.
     */
    @Override
    public SecretProvider create(Map<String, String> config) {

        return new VaultSecretProvider();
    }

    private void require(Map<String, String> config, String field)
    {
      if (!config.containsKey(field)) throw new RuntimeException("InternalSecretProviderFactory Missing required config field: " + field);
    }
}
