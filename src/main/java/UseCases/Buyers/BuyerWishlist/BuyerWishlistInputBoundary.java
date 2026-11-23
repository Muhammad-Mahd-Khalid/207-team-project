package UseCases.Buyers.BuyerWishlist;

import Entities.Item;

public interface BuyerWishlistInputBoundary {
    void addItem(Item selectedItem);
    void removeItem(String itemId);
    void viewWishlist();
}
