package UnitTests.Misc;

import static org.junit.jupiter.api.Assertions.*;

import DataAccess.APIDataAccessObject;
import java.io.IOException;
import org.junit.Test;

public class TestOAuthAPI {

  @Test
  public void OAuthAppTokenTest() {
    APIDataAccessObject testAccess = null;
    try {
      testAccess = new APIDataAccessObject("src/main/resources/ebay-config-default.yaml");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
