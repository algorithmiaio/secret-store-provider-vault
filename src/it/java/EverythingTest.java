
import com.algorithmia.plugin.vault.BasicSecretIdentifier;
import com.algorithmia.plugin.vault.VaultSecretProviderFactory;
import com.algorithmia.sdk.plugin.secrets.Secret;
import com.algorithmia.sdk.plugin.secrets.SecretIdentifier;
import com.algorithmia.sdk.plugin.secrets.SecretProvider;
import java.time.Instant;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EverythingTest
{
  public static SecretProvider provider; 

  @BeforeClass
  public static void loadProvider()
  {
    TreeMap<String, String> config = new TreeMap<>();

    config.put("vault_addr", "http://localhost:8200/");
    config.put("vault_token", "root-token-integration");
    config.put("vault_secret_path", "/secret/algorithmia");

    VaultSecretProviderFactory factory = new VaultSecretProviderFactory();

    provider = factory.create(config);

  }
  
  @Test
  public void testCreate()
  {
    SecretIdentifier id_a = provider.createSecret("llama");
    SecretIdentifier id_b = provider.createSecret("llama");

    Assert.assertNotEquals(id_a.toString(), id_b.toString());
    
  }

  @Test
  public void testCreateGet()
    throws Exception
  {
    SecretIdentifier id = provider.createSecret("llama");
    Secret sec = provider.getSecret(id);
    Assert.assertEquals("llama", sec.getValue());

   
  }
  @Test
  public void testCreateVersion()
    throws Exception
  {
    SecretIdentifier id = provider.createSecret("llama");
    String ver1 = provider.getSecretVersion(id);
    String ver2 = provider.getSecretVersion(id);

    Assert.assertEquals(ver1, ver2);
  }
 
  @Test
  public void testCreateUpdate()
    throws Exception
  {
    SecretIdentifier id = provider.createSecret("llama");
    Assert.assertEquals("llama", provider.getSecret(id).getValue());
    String ver1 = provider.getSecretVersion(id);

    String ver2 = provider.updateSecret(id, "lolwut");
    String ver3 = provider.getSecretVersion(id);
    Assert.assertEquals("lolwut", provider.getSecret(id).getValue());

    String ver4 = provider.updateSecret(id, "llama");
    Assert.assertEquals("llama", provider.getSecret(id).getValue());
    
    Assert.assertEquals("Return from update should equal get version", ver2, ver3);
    Assert.assertNotEquals("Update changes version", ver1, ver2);
    Assert.assertNotEquals("Version not based on value", ver1, ver4);

    Instant time = provider.secretLastUpdated(id);

  }

  @Test(expected = com.algorithmia.sdk.plugin.secrets.SecretNotFoundException.class)
  public void updateNegative()
    throws Exception
  {
    provider.getSecretVersion(new BasicSecretIdentifier("moo")); 
  }

  @Test
  public void testCreateDelete()
    throws Exception
  {
    SecretIdentifier id = provider.createSecret("llama");
    Assert.assertEquals("llama", provider.getSecret(id).getValue());

    Assert.assertTrue( provider.deleteSecret(id) );
    Assert.assertFalse( provider.deleteSecret(id) );
 
  }
}
