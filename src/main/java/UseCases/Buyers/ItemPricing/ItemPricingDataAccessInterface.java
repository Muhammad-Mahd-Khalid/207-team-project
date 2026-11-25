package UseCases.Buyers.ItemPricing;

import Entities.Item;
import java.io.IOException;
import java.util.ArrayList;

/** Interface for use in the API DAO for the ItemPricing use case. */
public interface ItemPricingDataAccessInterface {

  /** Returns a set number of Item entities for a given search via the eBay API. */
  ArrayList<Item> getItemsForSearchTerm(String searchTerm) throws IOException;
}
