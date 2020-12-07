package com.algorithmia.plugin;

import com.algorithmia.sdk.plugin.secrets.Secret;
import com.algorithmia.sdk.plugin.secrets.SecretIdentifier;
import com.algorithmia.sdk.plugin.secrets.SecretNotFoundException;
import com.algorithmia.sdk.plugin.secrets.SecretProvider;

import java.time.Instant;

public class InternalSecretProvider implements SecretProvider {
    @Override
    public Secret getSecret(SecretIdentifier id) throws SecretNotFoundException {
        return null;
    }

    @Override
    public SecretIdentifier createSecret(String value) {
        return null;
    }

    @Override
    public SecretIdentifier updateSecret(SecretIdentifier id, String value) throws SecretNotFoundException {
        return null;
    }

    @Override
    public boolean deleteSecret(SecretIdentifier id) throws SecretNotFoundException {
        return false;
    }

    @Override
    public Instant secretLastUpdated(SecretIdentifier id) {
        return null;
    }

    @Override
    public int getTtl() {
        return 0;
    }

    @Override
    public boolean allowsCreate() {
        return true;
    }

    @Override
    public boolean allowsUpdate() {
        return true;
    }

    @Override
    public boolean allowsDelete() {
        return true;
    }
}
