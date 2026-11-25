package InterfaceAdapters.Buyers.BuyerWishlist;

import UseCases.Buyers.BuyerWishlist.BuyerWishlistOutputData;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;

/** Holds the latest wishlist result for the panel. */
public class BuyerWishlistViewModel {
  private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
  private BuyerWishlistOutputData state =
      new BuyerWishlistOutputData(Collections.emptyList(), "", true);

  public BuyerWishlistOutputData getState() {
    return state;
  }

  /** Updates the result and notifies the panel. */
  public void setState(BuyerWishlistOutputData state) {
    this.state = state;
    changes.firePropertyChange("wishlist", null, state);
  }

  /** Registers a listener for result changes. */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    changes.addPropertyChangeListener(listener);
  }
}
