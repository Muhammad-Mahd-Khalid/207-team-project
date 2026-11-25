package UseCases.Buyers.BuyerWishlist;

import Entities.WishlistEntry;

/** Actions a buyer can take on the wishlist. */
public interface BuyerWishlistInputBoundary {
  /** Adds the selected listing if it is not already saved. */
  void addItem(WishlistEntry selection);

  /** Removes the saved listing with the given ID. */
  void removeItem(String itemId);

  /** Shows the stored wishlist. */
  void viewWishlist();
}
