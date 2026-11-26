package Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents a user of the Application. */
public class UserProfile {

  /**
   * Represents the role of the user as either a buyer or seller. Determines the view on consecutive
   * boot.
   */
  public enum UserRole {
    BUYER,
    SELLER
  }

  private final UserRole role;
  private final List<WishlistEntry> wishlist;

  /** Constructs a new user, given a role and a list of wishlist items. */
  public UserProfile(UserRole role, List<WishlistEntry> wishlist) {
    this.role = role;
    this.wishlist = new ArrayList<>(wishlist);
  }

  /** Returns the role of this UserProfile. */
  public UserRole getRole() {
    return role;
  }

  /** Returns a list of Wishlist Items for this user. */
  public List<WishlistEntry> getWishlist() {
    return Collections.unmodifiableList(new ArrayList<>(wishlist));
  }
}
