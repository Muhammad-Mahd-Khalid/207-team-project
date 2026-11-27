package UseCases.Sellers.SellingPriority;

import Entities.Item;
import java.io.IOException;
import java.util.List;

/** Interface for accessing comparable eBay listings for the Selling Priority use case. */
public interface SellingPriorityDataAccessInterface {

  /**
   * Returns eBay listings that are comparable to the seller's item.
   *
   * @param itemName the name of the item to search for
   * @param condition the condition of the seller's item
   * @param category the category of the seller's item
   * @return a list of comparable eBay Item entities
   */
  List<Item> getComparableItems(String itemName, String condition, String category)
      throws IOException;
}
