package UseCases.Buyers.BuyerWishlist;

import Entities.WishlistEntry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Handles adding, removing, and viewing saved listings. */
public class BuyerWishlistInteractor implements BuyerWishlistInputBoundary {
  private final BuyerWishlistDataAccessInterface store;
  private final BuyerWishlistOutputBoundary presenter;
  private List<WishlistEntry> wishlist = List.of();

  /** Creates the use case with its storage and presenter. */
  public BuyerWishlistInteractor(
      BuyerWishlistDataAccessInterface store, BuyerWishlistOutputBoundary presenter) {
    this.store = store;
    this.presenter = presenter;
  }

  @Override
  public void viewWishlist() {
    if (load()) {
      present(wishlist.isEmpty() ? "Your wishlist is empty" : "Wishlist loaded.", true);
    }
  }

  @Override
  public void addItem(WishlistEntry selection) {
    if (selection == null || !selection.isValid()) {
      present("Please select a valid item with a name, price, currency and eBay link.", false);
      return;
    }
    if (!load()) {
      return;
    }
    String id = selection.getItem().getItemId();
    for (WishlistEntry entry : wishlist) {
      if (entry.getItem().getItemId().equals(id)) {
        present("This item is already saved.", false);
        return;
      }
    }
    List<WishlistEntry> updated = new ArrayList<>(wishlist);
    updated.add(selection);
    save(updated, "Item saved to wishlist.");
  }

  @Override
  public void removeItem(String id) {
    if (id == null || id.isBlank()) {
      present("Please select a saved item to remove.", false);
      return;
    }
    if (!load()) {
      return;
    }
    List<WishlistEntry> updated = new ArrayList<>(wishlist);
    if (!updated.removeIf(entry -> entry.getItem().getItemId().equals(id))) {
      present("This item is not in your wishlist.", false);
      return;
    }
    save(updated, "Item removed from wishlist.");
  }

  private boolean load() {
    try {
      wishlist = store.loadItems();
      return true;
    } catch (IOException ex) {
      present(
          "Could not load your wishlist. Saved data has not been changed. Please retry.", false);
      return false;
    }
  }

  private void save(List<WishlistEntry> updated, String message) {
    try {
      store.saveItems(updated);
      wishlist = updated;
      present(message, true);
    } catch (IOException ex) {
      present(
          "Could not save your wishlist. Your previous wishlist has been kept. Please retry.",
          false);
    }
  }

  private void present(String message, boolean success) {
    presenter.present(new BuyerWishlistOutputData(wishlist, message, success));
  }
}
