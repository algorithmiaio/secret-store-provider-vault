package com.algorithmia.plugin.vault;

import com.algorithmia.sdk.plugin.secrets.Secret;
import com.algorithmia.sdk.plugin.secrets.SecretIdentifier;
import com.algorithmia.sdk.plugin.secrets.SecretNotFoundException;
import com.algorithmia.sdk.plugin.secrets.SecretProvider;
import com.algorithmia.sdk.plugin.secrets.SimpleSecret;
import com.google.protobuf.ByteString;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

public class VaultSecretProvider implements SecretProvider {

  @Override
  public Secret getSecret(SecretIdentifier id) throws SecretNotFoundException {
    return null;
  }

  @Override
  public String getSecretVersion(SecretIdentifier id) throws SecretNotFoundException {
    return null;
  }

  @Override
  public SecretIdentifier createSecret(String value) {

    UUID secret_id_uuid = UUID.randomUUID();
    UUID version_uuid = UUID.randomUUID();
    SecretIdentifier id = new BasicSecretIdentifier(secret_id_uuid.toString());
    return null;

  }

  @Override
  public String updateSecret(SecretIdentifier id, String value) throws SecretNotFoundException 
  {
    return null;
  }

  @Override
  public boolean deleteSecret(SecretIdentifier id) throws SecretNotFoundException 
  {
    // TODO
    return false;
  }

  @Override
  public Instant secretLastUpdated(SecretIdentifier id) throws SecretNotFoundException
  {
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
