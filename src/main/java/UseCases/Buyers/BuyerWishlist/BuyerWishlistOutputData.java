package UseCases.Buyers.BuyerWishlist;

import Entities.WishlistEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** The saved list and status message shown by the panel. */
public class BuyerWishlistOutputData {
  private final List<WishlistEntry> items;
  private final String message;
  private final boolean success;

  /** Creates a result with a snapshot of the current list. */
  public BuyerWishlistOutputData(List<WishlistEntry> items, String message, boolean success) {
    this.items = Collections.unmodifiableList(new ArrayList<>(items));
    this.message = message;
    this.success = success;
  }

  public List<WishlistEntry> getItems() {
    return items;
  }

  public String getMessage() {
    return message;
  }

  public boolean isSuccess() {
    return success;
  }
}
