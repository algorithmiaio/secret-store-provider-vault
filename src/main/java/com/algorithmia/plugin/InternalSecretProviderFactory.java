package com.algorithmia.plugin;

import com.algorithmia.sdk.plugin.secrets.SecretProvider;
import com.algorithmia.sdk.plugin.secrets.SecretProviderFactory;

import java.util.Map;

public class InternalSecretProviderFactory implements SecretProviderFactory {

    @Override
    public SecretProvider create(Map<String, String> config) {
        // This provider expects a database connection string that provides
        // access to a database where it can create/update its own table, as
        // well as a set of encryption keys for encrypting/decrypting values in
        // its own table
        return new InternalSecretProvider();
    }

}
