package com.algorithmia.plugin.sqlsecretprovider;

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

public class InternalSecretProvider implements SecretProvider {

  public static final String SQL_POOL_NAME="secret_jdbc_pool";

  private final CryptoKey key;

  public InternalSecretProvider(CryptoKey key)
  {
    this.key = key;

  }

  @Override
  public Secret getSecret(SecretIdentifier id) throws SecretNotFoundException 
  {

    try(Connection conn = DBUtil.openConnection(SQL_POOL_NAME))
    {
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM internal_secret_provider_secrets WHERE secret_id=?");
      ps.setString(1, id.getProviderKey());

      ResultSet rs = ps.executeQuery();
      while(rs.next())
      {
        String key_id = rs.getString("key_id");
        if (!key_id.equals(key.getKeyId()))
        {
          throw new SecretNotFoundException(String.format("For secret %s, key_id found %s, expected %s", 
            id.getProviderKey(), key_id, key.getKeyId()));
        }

        ByteString encrypted = ByteString.copyFrom(rs.getBytes("secret_data"));

        rs.close();
        ps.close();

        ByteString plain_data = CryptoUtil.decrypt(key, encrypted);

        return new SimpleSecret(new String(plain_data.toByteArray()), getTtl());
      }

    }
    catch(SQLException e){throw new RuntimeException(e);}
      
    throw new SecretNotFoundException("Not secret found: " + id.getProviderKey());
  }

  @Override
  public String getSecretVersion(SecretIdentifier id) throws SecretNotFoundException {
    try(Connection conn = DBUtil.openConnection(SQL_POOL_NAME))
    {
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM internal_secret_provider_secrets WHERE secret_id=?");
      ps.setString(1, id.getProviderKey());

      ResultSet rs = ps.executeQuery();
      while(rs.next())
      {
        return rs.getString("version");
      }

    }
    catch(SQLException e){throw new RuntimeException(e);}
      
    throw new SecretNotFoundException("Not secret found: " + id.getProviderKey());
  }

  @Override
  public SecretIdentifier createSecret(String value) {

    UUID secret_id_uuid = UUID.randomUUID();
    UUID version_uuid = UUID.randomUUID();
    SecretIdentifier id = new BasicSecretIdentifier(secret_id_uuid.toString());

    
    try(Connection conn = DBUtil.openConnection(SQL_POOL_NAME))
    {
      ByteString value_encrypted = CryptoUtil.encrypt(key, ByteString.copyFrom(value.getBytes("UTF-8")));

      PreparedStatement ps = conn.prepareStatement(
        "INSERT into internal_secret_provider_secrets "+
        "(secret_id, version, secret_data, key_id) " +
        "VALUES (?, ?, ?, ?)");
      ps.setString(1, id.getProviderKey());
      ps.setString(2, version_uuid.toString());
      ps.setBytes(3, value_encrypted.toByteArray());
      ps.setString(4, key.getKeyId());

      ps.execute();
      ps.close();

      return id;
    }
    catch(SQLException e){throw new RuntimeException(e);}
    catch(java.io.UnsupportedEncodingException e){throw new RuntimeException(e);}


  }

  @Override
  public String updateSecret(SecretIdentifier id, String value) throws SecretNotFoundException 
  {

    try(Connection conn = DBUtil.openConnection(SQL_POOL_NAME))
    {
      UUID version_uuid = UUID.randomUUID();
      ByteString value_encrypted = CryptoUtil.encrypt(key, ByteString.copyFrom(value.getBytes("UTF-8")));

      PreparedStatement ps = conn.prepareStatement("UPDATE internal_secret_provider_secrets SET version=?, secret_data=? WHERE secret_id=?");
      ps.setString(1, version_uuid.toString());
      ps.setBytes(2, value_encrypted.toByteArray());
      ps.setString(3, id.getProviderKey());

      int rows = ps.executeUpdate();

      if (rows == 0)
      {
        throw new SecretNotFoundException("Not secret found: " + id.getProviderKey());
      }

      return version_uuid.toString();

    }
    catch(SQLException e){throw new RuntimeException(e);}
    catch(java.io.UnsupportedEncodingException e){throw new RuntimeException(e);}
  }

  @Override
  public boolean deleteSecret(SecretIdentifier id) throws SecretNotFoundException 
  {
    try(Connection conn = DBUtil.openConnection(SQL_POOL_NAME))
    {
      PreparedStatement ps = conn.prepareStatement("DELETE FROM internal_secret_provider_secrets WHERE secret_id=?");
      ps.setString(1, id.getProviderKey());

      int rows = ps.executeUpdate();

      if (rows == 0)
      {
        return false;
      }
      else
      {
        return true;
      }
    }
    catch(SQLException e){throw new RuntimeException(e);}

  }

  @Override
  public Instant secretLastUpdated(SecretIdentifier id) throws SecretNotFoundException
  {
    try(Connection conn = DBUtil.openConnection(SQL_POOL_NAME))
    {
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM internal_secret_provider_secrets WHERE secret_id=?");
      ps.setString(1, id.getProviderKey());

      ResultSet rs = ps.executeQuery();
      while(rs.next())
      {
        return rs.getTimestamp("updated_at").toInstant();
      }

    }
    catch(SQLException e){throw new RuntimeException(e);}
      
    throw new SecretNotFoundException("Not secret found: " + id.getProviderKey());

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
