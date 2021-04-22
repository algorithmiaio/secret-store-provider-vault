package com.algorithmia.plugin.vault;

import com.algorithmia.sdk.plugin.secrets.Secret;
import com.algorithmia.sdk.plugin.secrets.SecretIdentifier;
import com.algorithmia.sdk.plugin.secrets.SecretNotFoundException;
import com.algorithmia.sdk.plugin.secrets.SecretProvider;
import com.algorithmia.sdk.plugin.secrets.SimpleSecret;
import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LogicalResponse;
import java.time.Instant;
import java.util.UUID;
import java.util.Map;
import java.util.TreeMap;

public class VaultSecretProvider implements SecretProvider {

  private final Vault vault;
  private final String secret_path;

  public VaultSecretProvider(Vault vault, String secret_path)
  {
    this.vault = vault;
    this.secret_path = secret_path;

  }


  private Map<String, String> getSecretInternal(SecretIdentifier id) 
    throws SecretNotFoundException
  {
    try
    {
      LogicalResponse resp = vault.logical().read( getPathForId(id) );
      if (resp.getData() == null)
      {
        throw new SecretNotFoundException("Not secret found: " + id.getProviderKey());
      }
      if (resp.getData().size() == 0)
      {
        throw new SecretNotFoundException("Not secret found: " + id.getProviderKey());

      }
      return resp.getData();

    }
    catch(VaultException e)
    {
      throw new RuntimeException(e);
    }

  }

  @Override
  public Secret getSecret(SecretIdentifier id) throws SecretNotFoundException {

    Map<String,String> data = getSecretInternal(id);
    return new SimpleSecret(data.get("value"), getTtl());
  
  }

  @Override
  public String getSecretVersion(SecretIdentifier id) throws SecretNotFoundException {
    
    Map<String,String> data = getSecretInternal(id);
    return data.get("version");
  }

  @Override
  public SecretIdentifier createSecret(String value) {

    UUID secret_id_uuid = UUID.randomUUID();
    UUID version_uuid = UUID.randomUUID();
    SecretIdentifier id = new BasicSecretIdentifier(secret_id_uuid.toString());

    Map<String, Object> store_data = new TreeMap<>();
    store_data.put("value", value);
    store_data.put("version", version_uuid.toString());
    store_data.put("update_time", "" + System.currentTimeMillis());

    try
    {
      vault.logical().write(getPathForId(id), store_data);
    }
    catch(VaultException e)
    {
      throw new RuntimeException(e);
    }
    return id;

  }

  @Override
  public String updateSecret(SecretIdentifier id, String value) throws SecretNotFoundException 
  {
    getSecretInternal(id);
    UUID version_uuid = UUID.randomUUID();

    Map<String, Object> store_data = new TreeMap<>();
    store_data.put("value", value);
    store_data.put("version", version_uuid.toString());
    store_data.put("update_time", "" + System.currentTimeMillis());

    try
    {
      vault.logical().write(getPathForId(id), store_data);
    }
    catch(VaultException e)
    {
      throw new RuntimeException(e);
    }
    return version_uuid.toString();

  }

  @Override
  public boolean deleteSecret(SecretIdentifier id) throws SecretNotFoundException 
  {

    // Seeing no clear way to see if a delete acted on existing
    // data or not, so just doing a read before the delete
    try
    {
      getSecretInternal(id);
    }
    catch(SecretNotFoundException e)
    {
      return false;
    }

    try
    {
      vault.logical().delete(getPathForId(id));
      return true;
    }
    catch(VaultException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Instant secretLastUpdated(SecretIdentifier id) throws SecretNotFoundException
  {
    Map<String,String> data = getSecretInternal(id);
    long update_time = Long.parseLong(data.get("update_time"));
    return Instant.ofEpochMilli(update_time);
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


  private String getPathForId(SecretIdentifier id)
  {
    return getPathForId(id.getProviderKey());
  }
  private String getPathForId(String id) {
    return secret_path + "/" + id;
  }
}
