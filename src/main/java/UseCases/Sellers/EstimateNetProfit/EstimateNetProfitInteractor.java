package UseCases.Sellers.EstimateNetProfit;

import Entities.Item;
import java.io.IOException;
import java.util.List;

/** Interactor for estimating the net profit of an eBay item. */
public class EstimateNetProfitInteractor {

  private final EstimateNetProfitDataAccessInterface dataAccess;

  /**
   * Creates an EstimateNetProfitInteractor with the given data access object.
   *
   * @param dataAccess the data access object used to retrieve market data
   */
  public EstimateNetProfitInteractor(EstimateNetProfitDataAccessInterface dataAccess) {
    this.dataAccess = dataAccess;
  }

  /**
   * Estimates the net profit of an item based on the seller's input.
   *
   * @param inputData the input data for the estimate
   * @return the estimated net profit analysis
   * @throws IOException if the market data cannot be retrieved
   */
  public EstimateNetProfitOutputData execute(EstimateNetProfitInputData inputData)
      throws IOException {

    List<Item> marketItems = dataAccess.getItemsForSearchTerm(inputData.getSearchTerm());

    double saleAmount = inputData.getTargetSellingPrice() + inputData.getBuyerShippingCharge();

    double ebayFee = calculateEbayFee(saleAmount);
    double totalSellingCost =
        ebayFee + inputData.getSellerShippingCost() + inputData.getPackagingCost();

    double netProfit = inputData.getTargetSellingPrice() - totalSellingCost;

    double profitMargin = 0.0;
    if (inputData.getTargetSellingPrice() != 0.0) {
      profitMargin = (netProfit / inputData.getTargetSellingPrice()) * 100.0;
    }

    double lowestMarketPrice = 0.0;
    double highestMarketPrice = 0.0;
    double averageMarketPrice = 0.0;
    int numberOfListings = marketItems.size();

    if (!marketItems.isEmpty()) {
      lowestMarketPrice = Double.MAX_VALUE;
      highestMarketPrice = Double.MIN_VALUE;

      double totalMarketPrice = 0.0;

      for (Item item : marketItems) {
        double price = item.getPrice();

        lowestMarketPrice = Math.min(lowestMarketPrice, price);
        highestMarketPrice = Math.max(highestMarketPrice, price);
        totalMarketPrice += price;
      }

      averageMarketPrice = totalMarketPrice / numberOfListings;
    }

    return new EstimateNetProfitOutputData(
        ebayFee,
        totalSellingCost,
        netProfit,
        profitMargin,
        lowestMarketPrice,
        highestMarketPrice,
        averageMarketPrice,
        numberOfListings);
  }

  private double calculateEbayFee(double saleAmount) {
    double fee;

    if (saleAmount <= 7500.0) {
      fee = saleAmount * 0.136;
    } else {
      fee = 7500.0 * 0.136 + (saleAmount - 7500.0) * 0.0235;
    }

    if (saleAmount <= 10.0) {
      fee += 0.30;
    } else {
      fee += 0.40;
    }

    return fee;
  }
}
