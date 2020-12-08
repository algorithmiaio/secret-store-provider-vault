package com.algorithmia.plugin.sqlsecretprovider;

import com.algorithmia.sdk.plugin.secrets.SecretProvider;
import com.algorithmia.sdk.plugin.secrets.SecretProviderFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public class InternalSecretProviderFactory implements SecretProviderFactory {


    /**
     * Should we check the database before we return?  Probably.
     */
    @Override
    public SecretProvider create(Map<String, String> config) {
      try
      {
        loadJDBCPool(config);
        setupTables();

        // This provider expects a database connection string that provides
        // access to a database where it can create/update its own table, as
        // well as a set of encryption keys for encrypting/decrypting values in
        // its own table
        return new InternalSecretProvider(CryptoUtil.loadKey(config));
      }
      catch(java.sql.SQLException e)
      {
        throw new RuntimeException(e);
      }
    }

    private void loadJDBCPool(Map<String, String> config) 
      throws java.sql.SQLException
    {
      
      String driver = "com.mysql.cj.jdbc.Driver";
      String uri = config.get("db_uri");
      String username = config.get("db_username");
      String password = config.get("db_password");

      DBUtil.openConnectionPool( InternalSecretProvider.SQL_POOL_NAME , driver, uri, username, password, 16, 4);
    }

    private void setupTables()
      throws java.sql.SQLException
    {
      try(Connection conn = DBUtil.openConnection( InternalSecretProvider.SQL_POOL_NAME ))
      {
        PreparedStatement ps = conn.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS internal_secret_provider_secrets (" +
                        "  secret_id VARCHAR(64) UNIQUE NOT NULL," +
                        "  version VARCHAR(64) NOT NULL," +
                        "  secret_data BLOB  NOT NULL," +
                        "  key_id VARCHAR(64) NOT NULL," +
                        "  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                        "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "  PRIMARY KEY(secret_id))");
        ps.execute();

      }


    }

}
