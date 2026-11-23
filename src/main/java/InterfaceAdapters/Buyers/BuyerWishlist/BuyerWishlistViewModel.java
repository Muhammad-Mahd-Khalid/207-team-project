package InterfaceAdapters.Buyers.BuyerWishlist;

import UseCases.Buyers.BuyerWishlist.BuyerWishlistOutputData;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;

public class BuyerWishlistViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private BuyerWishlistOutputData state =
            new BuyerWishlistOutputData(Collections.emptyList(), "", true);

    public BuyerWishlistOutputData getState() { return state; }
    public void setState(BuyerWishlistOutputData state) {
        this.state = state;
        changes.firePropertyChange("wishlist", null, state);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }
}
