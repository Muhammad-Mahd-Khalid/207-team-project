package UseCases.Buyers.BuyerWishlist;

/** Receives the result of a wishlist request. */
public interface BuyerWishlistOutputBoundary {
  /** Shows the current list and the outcome of the request. */
  void present(BuyerWishlistOutputData result);
}
