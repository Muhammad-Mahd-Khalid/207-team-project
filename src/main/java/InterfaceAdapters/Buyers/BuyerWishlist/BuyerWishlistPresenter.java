package InterfaceAdapters.Buyers.BuyerWishlist;

import UseCases.Buyers.BuyerWishlist.BuyerWishlistOutputBoundary;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistOutputData;

/** Passes the use case result to the view model. */
public class BuyerWishlistPresenter implements BuyerWishlistOutputBoundary {
  private final BuyerWishlistViewModel model;

  /** Creates a presenter for the supplied view model. */
  public BuyerWishlistPresenter(BuyerWishlistViewModel model) {
    this.model = model;
  }

  @Override
  public void present(BuyerWishlistOutputData result) {
    model.setState(result);
  }
}
