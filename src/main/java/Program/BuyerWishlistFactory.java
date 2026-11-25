package Program;

import DataAccess.JsonBuyerWishlistDataAccessObject;
import InterfaceAdapters.Buyers.BuyerWishlist.BuyerWishlistController;
import InterfaceAdapters.Buyers.BuyerWishlist.BuyerWishlistPresenter;
import InterfaceAdapters.Buyers.BuyerWishlist.BuyerWishlistViewModel;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistInteractor;
import Views.Buyers.BuyerWishlist.BuyerWishlistPanel;
import java.nio.file.Path;

/** Creates a wishlist panel backed by a local file. */
public final class BuyerWishlistFactory {
  private BuyerWishlistFactory() {}

  /** Call on the Swing event dispatch thread. The caller chooses the local profile file. */
  public static BuyerWishlistPanel create(Path file) {
    BuyerWishlistViewModel model = new BuyerWishlistViewModel();
    BuyerWishlistInteractor useCase =
        new BuyerWishlistInteractor(
            new JsonBuyerWishlistDataAccessObject(file), new BuyerWishlistPresenter(model));
    BuyerWishlistPanel panel = new BuyerWishlistPanel(new BuyerWishlistController(useCase), model);
    panel.refreshWishlist();
    return panel;
  }
}
