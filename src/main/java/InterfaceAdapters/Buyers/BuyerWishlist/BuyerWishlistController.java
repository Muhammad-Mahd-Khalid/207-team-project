package InterfaceAdapters.Buyers.BuyerWishlist;

import Entities.WishlistEntry;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistInputBoundary;

/** Connects the wishlist buttons to the use case. */
public class BuyerWishlistController {
  private final BuyerWishlistInputBoundary useCase;

  /** Creates a controller for the supplied use case. */
  public BuyerWishlistController(BuyerWishlistInputBoundary useCase) {
    this.useCase = useCase;
  }

  /** Saves the selected listing. */
  public void addItem(WishlistEntry selection) {
    useCase.addItem(selection);
  }

  /** Removes a listing by its item ID. */
  public void removeItem(String id) {
    useCase.removeItem(id);
  }

  /** Loads the saved wishlist. */
  public void viewWishlist() {
    useCase.viewWishlist();
  }
}
