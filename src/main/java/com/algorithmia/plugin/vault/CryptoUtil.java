package com.algorithmia.plugin.sqlsecretprovider;

import com.google.protobuf.ByteString;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class CryptoUtil
{
  public static final String MODE="AES/CBC/PKCS5PADDING";
  public static final int IV_SIZE=16;


  public static CryptoKey loadKey(Map<String, String> config)
  {
    String key_string = config.get("secret_key");

    try
    {

      // Hash the given input string, do a sha256 on it making
      // 32 bytes.
      // Use the first 16 as the key id
      // use the second 16 as the key itself

      MessageDigest md = MessageDigest.getInstance("SHA-256");

      byte[] hash = md.digest(key_string.getBytes());

      ByteString hash_s = ByteString.copyFrom(hash);
      ByteString key_id = hash_s.substring(0,16);
      ByteString key_data = hash_s.substring(16);

      return new CryptoKey(getHex(key_id), key_data);

    }
    catch(java.security.NoSuchAlgorithmException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static ByteString encrypt(CryptoKey key, ByteString plain)
  {
    try
    {
      Random rnd = new Random();
      byte iv[]=new byte[IV_SIZE];
      rnd.nextBytes(iv);

      Cipher c = Cipher.getInstance(MODE);

      IvParameterSpec iv_spec = new IvParameterSpec(iv);

      Key k = new SecretKeySpec(key.getKeyData().toByteArray(), "AES");

      c.init(Cipher.ENCRYPT_MODE, k, iv_spec);

      ByteString encrypted = ByteString.copyFrom(c.doFinal( plain.toByteArray() ));

      return ByteString.copyFrom(iv).concat(encrypted);

    }
    catch(java.security.GeneralSecurityException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static ByteString decrypt(CryptoKey key, ByteString cipher)
  {
    try
    {
      ByteString iv_bs = cipher.substring(0, IV_SIZE);
      ByteString data = cipher.substring(IV_SIZE);

      Cipher c = Cipher.getInstance(MODE);

      IvParameterSpec iv_spec = new IvParameterSpec(iv_bs.toByteArray());

      Key k = new SecretKeySpec(key.getKeyData().toByteArray(), "AES");

      c.init(Cipher.DECRYPT_MODE, k, iv_spec);

      return ByteString.copyFrom(c.doFinal( data.toByteArray() ));

    }
    catch(java.security.GeneralSecurityException e)
    {
      throw new RuntimeException(e);
    }

  }

  public static String getHex(ByteString bs)
  {
    Hex h = new Hex();
    return new String(h.encodeHex(bs.toByteArray()));
  }


}
