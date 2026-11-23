package InterfaceAdapters.Buyers.BuyerWishlist;

import UseCases.Buyers.BuyerWishlist.BuyerWishlistOutputBoundary;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistOutputData;

public class BuyerWishlistPresenter implements BuyerWishlistOutputBoundary {
    private final BuyerWishlistViewModel model;
    public BuyerWishlistPresenter(BuyerWishlistViewModel model) { this.model = model; }
    @Override
    public void present(BuyerWishlistOutputData result) { model.setState(result); }
}
