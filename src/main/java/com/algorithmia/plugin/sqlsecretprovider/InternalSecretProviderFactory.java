package com.algorithmia.plugin.sqlsecretprovider;

import com.algorithmia.sdk.plugin.secrets.SecretProvider;
import com.algorithmia.sdk.plugin.secrets.SecretProviderFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public class InternalSecretProviderFactory implements SecretProviderFactory {


    /**
     * After this returns, the database has been connected to and tables created as needed.
     */
    @Override
    public SecretProvider create(Map<String, String> config) {

//      require(config, "db_uri");
//      require(config, "db_username");
//      require(config, "db_password");
//      require(config, "secret_key");
//      try
//      {
//        loadJDBCPool(config);
//
//        // This provider expects a database connection string that provides
//        // access to a database where it can create/update its own table, as
//        // well as a set of encryption keys for encrypting/decrypting values in
//        // its own table
//        return new InternalSecretProvider(CryptoUtil.loadKey(config));
//      }
//      catch(java.sql.SQLException e)
//      {
//        throw new RuntimeException(e);
//      }
        return new InternalSecretProvider();
    }

//    private void require(Map<String, String> config, String field)
//    {
//      if (!config.containsKey(field)) throw new RuntimeException("InternalSecretProviderFactory Missing required config field: " + field);
//    }
//
//    private void loadJDBCPool(Map<String, String> config)
//      throws java.sql.SQLException
//    {
//      String driver = "com.mysql.cj.jdbc.Driver";
//      String uri = config.get("db_uri");
//      String username = config.get("db_username");
//      String password = config.get("db_password");
//
//      DBUtil.openConnectionPool( InternalSecretProvider.SQL_POOL_NAME , driver, uri, username, password, 16, 4);
//    }
}
