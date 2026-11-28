package UseCases.Sellers.EstimateNetProfit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Entities.Item;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

/** Tests for the EstimateNetProfitInteractor. */
class EstimateNetProfitInteractorTest {

  /** Tests the market price statistics calculation. */
  @Test
  void testMarketPriceStatistics() throws Exception {
    ArrayList<Item> items = new ArrayList<>();

    items.add(new Item("1", "Item 1", 100.0, "CAD", "New", "Electronics"));
    items.add(new Item("2", "Item 2", 200.0, "CAD", "New", "Electronics"));
    items.add(new Item("3", "Item 3", 300.0, "CAD", "New", "Electronics"));

    EstimateNetProfitDataAccessInterface dataAccess = searchTerm -> items;

    EstimateNetProfitInteractor interactor = new EstimateNetProfitInteractor(dataAccess);

    EstimateNetProfitInputData input =
        new EstimateNetProfitInputData("headphones", 250.0, 20.0, 5.0);

    EstimateNetProfitOutputData output = interactor.execute(input);

    assertEquals(100.0, output.getLowestMarketPrice());
    assertEquals(300.0, output.getHighestMarketPrice());
    assertEquals(200.0, output.getAverageMarketPrice());
    assertEquals(3, output.getNumberOfListings());
  }

  /** Tests the net profit calculation. */
  @Test
  void testNetProfitCalculation() throws Exception {
    ArrayList<Item> items = new ArrayList<>();

    EstimateNetProfitDataAccessInterface dataAccess = searchTerm -> items;

    EstimateNetProfitInteractor interactor = new EstimateNetProfitInteractor(dataAccess);

    EstimateNetProfitInputData input =
        new EstimateNetProfitInputData("headphones", 250.0, 20.0, 5.0);

    EstimateNetProfitOutputData output = interactor.execute(input);

    assertEquals(36.8, output.getEbayFee(), 0.01);
    assertEquals(61.8, output.getTotalSellingCost(), 0.01);
    assertEquals(188.2, output.getNetProfit(), 0.01);
    assertEquals(75.28, output.getProfitMargin(), 0.01);
  }
}
