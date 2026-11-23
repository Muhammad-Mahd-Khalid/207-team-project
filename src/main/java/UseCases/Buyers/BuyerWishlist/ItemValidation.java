package UseCases.Buyers.BuyerWishlist;

import Entities.Item;
import java.net.URI;

/** Shared validation for incoming selections and saved listing snapshots. */
public final class ItemValidation {
    private ItemValidation() { }
    public static boolean isValidItem(Item item) {
        if (item == null || blank(item.getId()) || blank(item.getName())
                || item.getPrice() == null || item.getPrice().signum() < 0
                || item.getCurrency() == null || !item.getCurrency().matches("[A-Z]{3}")
                || blank(item.getEbayUrl())) { return false; }
        try {
            URI link = URI.create(item.getEbayUrl());
            return ("https".equalsIgnoreCase(link.getScheme()) || "http".equalsIgnoreCase(link.getScheme()))
                    && link.getHost() != null;
        } catch (IllegalArgumentException ex) { return false; }
    }

    private static boolean blank(String text) { return text == null || text.isBlank(); }
}
