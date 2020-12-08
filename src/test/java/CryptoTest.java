
import com.algorithmia.plugin.sqlsecretprovider.CryptoKey;
import com.algorithmia.plugin.sqlsecretprovider.CryptoUtil;
import com.google.protobuf.ByteString;
import java.util.Random;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.Test;

public class CryptoTest
{

  @Test
  public void testNothing()
  {


  }

  @Test
  public void testEncryptDecrypt()
  {
    Random rnd = new Random();

    TreeMap<String, String> config = new TreeMap<>();
    config.put("secret_key", "meow-" + rnd.nextLong());

    CryptoKey key = CryptoUtil.loadKey(config);

    for(int sz=0; sz<8192; sz++)
    {
      byte in[]=new byte[sz];
      rnd.nextBytes(in);
      ByteString in_bs = ByteString.copyFrom(in);
      
      ByteString encrypted_bs = CryptoUtil.encrypt(key, in_bs);
      ByteString encrypted_bs2 = CryptoUtil.encrypt(key, in_bs);


      // They have different IVs
      Assert.assertNotEquals( CryptoUtil.getHex(encrypted_bs), CryptoUtil.getHex(encrypted_bs2));

      ByteString out_bs = CryptoUtil.decrypt(key, encrypted_bs);

      Assert.assertEquals( CryptoUtil.getHex(in_bs), CryptoUtil.getHex(out_bs));
    }

  }
}
