package UseCases.Buyers.BuyerWishlist;

import Entities.WishlistEntry;

public interface BuyerWishlistInputBoundary {
    void addItem(WishlistEntry selection);
    void removeItem(String itemId);
    void viewWishlist();
}
