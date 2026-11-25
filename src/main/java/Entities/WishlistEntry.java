package Entities;

import java.net.URI;

/** A saved listing includes the link shown in the search results. */
public class WishlistEntry {
  private final Item item;
  private final String listingLink;

  /** Constructs a new WishlistItem given an Item, and it's link on eBay. */
  public WishlistEntry(Item item, String listingLink) {
    this.item = item;
    this.listingLink = listingLink;
  }

  /** Returns this wishlist entry's item object. */
  public Item getItem() {
    return item;
  }

  /** Returns this wishlist entry's listing link on eBay. */
  public String getListingLink() {
    return listingLink;
  }

  /**
   * Checks if this wishlist entry is valid by matching all of its fields to expect values, and
   * checking the link.
   */
  public boolean isValid() {
    if (item == null
        || blank(item.getItemId())
        || blank(item.getTitle())
        || !Double.isFinite(item.getPrice())
        || item.getPrice() < 0
        || item.getCurrency() == null
        || !item.getCurrency().matches("[A-Z]{3}")
        || blank(listingLink)) {
      return false;
    }
    try {
      URI link = URI.create(listingLink);
      return link.getHost() != null
          && ("https".equalsIgnoreCase(link.getScheme())
              || "http".equalsIgnoreCase(link.getScheme()));
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }

  private boolean blank(String text) {
    return text == null || text.isBlank();
  }
}
