package Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProfile {

    public enum UserRole {
        BUYER,
        SELLER
    }

    private final UserRole role;
    private final List<WishlistEntry> wishlist;

    public UserProfile(UserRole role, List<WishlistEntry> wishlist) {
        this.role = role;
        this.wishlist = new ArrayList<>(wishlist);
    }

    public UserRole getRole() {
        return role;
    }

    public List<WishlistEntry> getWishlist() {
        return Collections.unmodifiableList(new ArrayList<>(wishlist));
    }
}
