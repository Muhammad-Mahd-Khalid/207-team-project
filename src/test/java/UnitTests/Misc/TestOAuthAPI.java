package UnitTests.Misc;

import DataAccess.APIDataAccessObject;
import java.io.IOException;
import org.junit.Test;

/** Placeholder Tests for the API DAO. */
public class TestOAuthAPI {

  @Test
  /* Quick and dirty test to check whether the API DAO is authing with the eBay OAuth API or not. */
  public void OAuthAppTokenTest() {
    APIDataAccessObject testAccess = null;
    try {
      testAccess = new APIDataAccessObject("src/main/resources/ebay-config-default.yaml");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
