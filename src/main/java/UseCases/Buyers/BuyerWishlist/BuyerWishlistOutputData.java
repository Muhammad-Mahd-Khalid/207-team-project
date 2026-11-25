package UseCases.Buyers.BuyerWishlist;

import Entities.WishlistEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyerWishlistOutputData {
    private final List<WishlistEntry> items;
    private final String message;
    private final boolean success;

    public BuyerWishlistOutputData(List<WishlistEntry> items, String message, boolean success) {
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
        this.message = message;
        this.success = success;
    }

    public List<WishlistEntry> getItems() { return items; }
    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }
}
