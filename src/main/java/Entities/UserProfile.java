package Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** The local buyer's saved items. Account login is outside this use case. */
public class UserProfile {
    private final List<Item> wishlist;

    public UserProfile(List<Item> wishlist) {
        this.wishlist = new ArrayList<>(wishlist);
    }

    public List<Item> getWishlist() {
        return Collections.unmodifiableList(new ArrayList<>(wishlist));
    }

    public boolean containsItem(String id) {
        return wishlist.stream().anyMatch(item -> item.getId().equals(id));
    }

    public void addItem(Item item) { wishlist.add(item); }
    public boolean removeItem(String id) {
        return wishlist.removeIf(item -> item.getId().equals(id));
    }
}
