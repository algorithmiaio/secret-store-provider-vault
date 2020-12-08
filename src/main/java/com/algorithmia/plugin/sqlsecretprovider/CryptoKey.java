package com.algorithmia.plugin.sqlsecretprovider;

import com.google.protobuf.ByteString;

public class CryptoKey
{
  private final String key_id;
  private final ByteString key_data;

  public CryptoKey(String id, ByteString data)
  {
    this.key_data = data;
    this.key_id = id;
  }

  public String getKeyId() { return key_id; }
  public ByteString getKeyData() { return key_data; }
}
