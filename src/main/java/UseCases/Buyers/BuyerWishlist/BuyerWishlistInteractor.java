package UseCases.Buyers.BuyerWishlist;

import Entities.Item;
import Entities.UserProfile;
import java.io.IOException;
import java.util.Collections;

public class BuyerWishlistInteractor implements BuyerWishlistInputBoundary {
    private final BuyerWishlistDataAccessInterface store;
    private final BuyerWishlistOutputBoundary presenter;
    private UserProfile profile = new UserProfile(Collections.emptyList());

    public BuyerWishlistInteractor(BuyerWishlistDataAccessInterface store,
                                   BuyerWishlistOutputBoundary presenter) {
        this.store = store;
        this.presenter = presenter;
    }

    @Override
    public void viewWishlist() {
        if (load()) {
            present(profile.getWishlist().isEmpty() ? "Your wishlist is empty" : "Wishlist loaded.", true);
        }
    }

    @Override
    public void addItem(Item item) {
        if (!ItemValidation.isValidItem(item)) {
            present("Please select a valid item with a name, price, currency and eBay link.", false);
            return;
        }
        if (!load()) { return; }
        if (profile.containsItem(item.getId())) {
            present("This item is already saved.", false);
            return;
        }
        UserProfile updated = new UserProfile(profile.getWishlist());
        updated.addItem(item);
        save(updated, "Item saved to wishlist.");
    }

    @Override
    public void removeItem(String id) {
        if (id == null || id.isBlank()) {
            present("Please select a saved item to remove.", false);
            return;
        }
        if (!load()) { return; }
        UserProfile updated = new UserProfile(profile.getWishlist());
        if (!updated.removeItem(id)) {
            present("This item is not in your wishlist.", false);
            return;
        }
        save(updated, "Item removed from wishlist.");
    }

    private boolean load() {
        try {
            profile = new UserProfile(store.loadItems());
            return true;
        } catch (IOException ex) {
            present("Could not load your wishlist. Saved data has not been changed. Please retry.", false);
            return false;
        }
    }

    private void save(UserProfile updated, String message) {
        try {
            store.saveItems(updated.getWishlist());
            profile = updated;
            present(message, true);
        } catch (IOException ex) {
            present("Could not save your wishlist. Your previous wishlist has been kept. Please retry.", false);
        }
    }

    private void present(String message, boolean success) {
        presenter.present(new BuyerWishlistOutputData(profile.getWishlist(), message, success));
    }

}
