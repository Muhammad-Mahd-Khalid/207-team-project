package UseCases.Buyers.BuyerWishlist;

import Entities.WishlistEntry;
import java.io.IOException;
import java.util.List;

/** A failed save must leave the previously saved wishlist intact. */
public interface BuyerWishlistDataAccessInterface {
    List<WishlistEntry> loadItems() throws IOException;
    void saveItems(List<WishlistEntry> items) throws IOException;
}
