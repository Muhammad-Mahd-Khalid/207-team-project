package UseCases.Buyers.BuyerWishlist;

import Entities.Item;
import java.io.IOException;
import java.util.List;

/** A failed save must leave the previously saved wishlist intact. */
public interface BuyerWishlistDataAccessInterface {
    List<Item> loadItems() throws IOException;
    void saveItems(List<Item> items) throws IOException;
}
