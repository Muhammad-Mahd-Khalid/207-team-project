package DataAccess;

import Entities.Item;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistDataAccessInterface;
import UseCases.Buyers.BuyerWishlist.ItemValidation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.math.BigDecimal;
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
    private final ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    public JsonBuyerWishlistDataAccessObject(Path file) {
        this.file = file.toAbsolutePath();
    }

    @Override
    public List<Item> loadItems() throws IOException {
        if (Files.notExists(file)) { return new ArrayList<>(); }
        JsonNode root = mapper.readTree(file.toFile());
        if (root == null || !root.isArray()) { throw new IOException("Invalid wishlist file."); }
        List<Item> items = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (JsonNode row : root) {
            Item item;
            try {
                item = new Item(text(row, "id"), text(row, "name"),
                        new BigDecimal(text(row, "price")), text(row, "currency"), text(row, "ebayUrl"));
            } catch (IllegalArgumentException ex) {
                throw new IOException("Invalid saved item.", ex);
            }
            if (!ItemValidation.isValidItem(item) || !ids.add(item.getId())) {
                throw new IOException("Invalid or duplicate saved item.");
            }
            items.add(item);
        }
        return items;
    }

    private String text(JsonNode row, String field) throws IOException {
        JsonNode value = row.get(field);
        if (value == null || !value.isTextual()) { throw new IOException("Missing item field: " + field); }
        return value.textValue();
    }

    @Override
    public void saveItems(List<Item> items) throws IOException {
        ArrayNode root = mapper.createArrayNode();
        Set<String> ids = new HashSet<>();
        for (Item item : items) {
            if (!ItemValidation.isValidItem(item) || !ids.add(item.getId())) {
                throw new IOException("Cannot save invalid or duplicate items.");
            }
            ObjectNode row = root.addObject();
            row.put("id", item.getId());
            row.put("name", item.getName());
            row.put("price", item.getPrice().toPlainString());
            row.put("currency", item.getCurrency());
            row.put("ebayUrl", item.getEbayUrl());
        }
        Files.createDirectories(file.getParent());
        Path temporary = Files.createTempFile(file.getParent(), "wishlist-", ".tmp");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), root);
            // Do not fall back to a non-atomic replacement: failure must preserve the old file.
            Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
