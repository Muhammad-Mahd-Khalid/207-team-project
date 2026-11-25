package UseCases.Buyers.BuyerWishlist;

import Entities.WishlistEntry;
import java.io.IOException;
import java.util.List;

/** A failed save must leave the previously saved wishlist intact. */
public interface BuyerWishlistDataAccessInterface {
  /** Returns the saved items in display order. */
  List<WishlistEntry> loadItems() throws IOException;

  /** Replaces the saved list with the supplied items. */
  void saveItems(List<WishlistEntry> items) throws IOException;
}
