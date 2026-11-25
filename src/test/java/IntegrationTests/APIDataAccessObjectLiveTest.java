package IntegrationTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import DataAccess.APIDataAccessObject;
import Entities.Item;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Live integration test that hits the real eBay Browse API. Requires a valid eBay config YAML file
 * on disk. Skips automatically (via JUnit assumption) when the file is missing, so a fresh clone or
 * a CI run without secrets will not fail.
 */
@Tag("integration")
class APIDataAccessObjectLiveTest {

  private static final String DEFAULT_CONFIG_PATH = "src/main/resources/ebay-config-default.yaml";

  private static final String CONFIG_PATH =
      System.getenv().getOrDefault("EBAY_CONFIG_PATH", DEFAULT_CONFIG_PATH);

  @Test
  void searchReturnsRealItemsAndBuildsEntities() throws IOException {
    assumeTrue(
        new File(CONFIG_PATH).isFile(),
        "eBay config file not found at " + CONFIG_PATH + "; skipping live test.");

    APIDataAccessObject dao = new APIDataAccessObject(CONFIG_PATH);

    List<Item> items = dao.getItemsForSearchTerm("camera");

    assertFalse(items.isEmpty(), "Expected at least one item for this search.");

    Item first = items.get(0);
    assertNotNull(first.getItemId(), "itemId should be mapped");
    assertNotNull(first.getTitle(), "title should be mapped");
    assertNotNull(first.getCurrency(), "currency should be mapped");
    assertTrue(first.getPrice() > 0, "price should be a positive number");
  }
}
