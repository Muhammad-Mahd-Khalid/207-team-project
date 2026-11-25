package DataAccess;

import Entities.Item;
import Entities.WishlistEntry;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistDataAccessInterface;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Stores a single local profile's wishlist, without calling the eBay API. */
public class JsonBuyerWishlistDataAccessObject implements BuyerWishlistDataAccessInterface {
  private final Path file;
  private final ObjectMapper mapper =
      new ObjectMapper().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

  /** Uses the given file to store the wishlist. */
  public JsonBuyerWishlistDataAccessObject(Path file) {
    this.file = file.toAbsolutePath();
  }

  @Override
  public List<WishlistEntry> loadItems() throws IOException {
    if (Files.notExists(file)) {
      return new ArrayList<>();
    }
    JsonNode root = mapper.readTree(file.toFile());
    if (root == null || !root.isArray()) {
      throw new IOException("Invalid wishlist file.");
    }
    List<WishlistEntry> items = new ArrayList<>();
    Set<String> ids = new HashSet<>();
    for (JsonNode row : root) {
      try {
        Item item =
            new Item(
                text(row, "id"),
                text(row, "name"),
                Double.parseDouble(text(row, "price")),
                text(row, "currency"),
                row.path("condition").asText(null),
                row.path("category").asText(null));
        WishlistEntry entry = new WishlistEntry(item, text(row, "ebayUrl"));
        if (!entry.isValid() || !ids.add(item.getItemId())) {
          throw new IOException("Invalid or duplicate saved item.");
        }
        items.add(entry);
      } catch (NumberFormatException ex) {
        throw new IOException("Invalid saved price.", ex);
      }
    }
    return items;
  }

  private String text(JsonNode row, String field) throws IOException {
    JsonNode value = row.get(field);
    if (value == null || !value.isTextual()) {
      throw new IOException("Missing item field: " + field);
    }
    return value.textValue();
  }

  @Override
  public void saveItems(List<WishlistEntry> items) throws IOException {
    ArrayNode root = mapper.createArrayNode();
    Set<String> ids = new HashSet<>();
    for (WishlistEntry entry : items) {
      if (!entry.isValid() || !ids.add(entry.getItem().getItemId())) {
        throw new IOException("Cannot save invalid or duplicate items.");
      }
      Item item = entry.getItem();
      ObjectNode row = root.addObject();
      row.put("id", item.getItemId());
      row.put("name", item.getTitle());
      row.put("price", Double.toString(item.getPrice()));
      row.put("currency", item.getCurrency());
      row.put("ebayUrl", entry.getListingLink());
      row.put("condition", item.getCondition());
      row.put("category", item.getCategory());
    }
    Files.createDirectories(file.getParent());
    Path temporary = Files.createTempFile(file.getParent(), "wishlist-", ".tmp");
    try {
      mapper.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), root);
      // Do not fall back to a non-atomic replacement: failure must preserve the old file.
      Files.move(
          temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    } finally {
      Files.deleteIfExists(temporary);
    }
  }
}
