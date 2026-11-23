package InterfaceAdapters.Buyers.BuyerWishlist;

import Entities.Item;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistInputBoundary;

public class BuyerWishlistController {
    private final BuyerWishlistInputBoundary useCase;
    public BuyerWishlistController(BuyerWishlistInputBoundary useCase) { this.useCase = useCase; }
    public void addItem(Item selectedItem) { useCase.addItem(selectedItem); }
    public void removeItem(String id) { useCase.removeItem(id); }
    public void viewWishlist() { useCase.viewWishlist(); }
}
