package UseCases.Sellers.EstimateNetProfit;

import Entities.Item;
import java.io.IOException;
import java.util.List;

/** Interface for use in the API DAO for the EstimateNetProfit use case. */
public interface EstimateNetProfitDataAccessInterface {

  /** Returns Item entities matching a given search via the eBay API. */
  List<Item> getItemsForSearchTerm(String searchTerm) throws IOException;
}
