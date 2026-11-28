package UseCases.Sellers.EstimateNetProfit;

/** Stores the results of the estimated net profit analysis. */
public class EstimateNetProfitOutputData {

  private final double ebayFee;
  private final double totalSellingCost;
  private final double netProfit;
  private final double profitMargin;

  private final double lowestMarketPrice;
  private final double highestMarketPrice;
  private final double averageMarketPrice;
  private final int numberOfListings;

  /** Creates output data for the Estimate Net Profit use case. */
  public EstimateNetProfitOutputData(
      double ebayFee,
      double totalSellingCost,
      double netProfit,
      double profitMargin,
      double lowestMarketPrice,
      double highestMarketPrice,
      double averageMarketPrice,
      int numberOfListings) {
    this.ebayFee = ebayFee;
    this.totalSellingCost = totalSellingCost;
    this.netProfit = netProfit;
    this.profitMargin = profitMargin;
    this.lowestMarketPrice = lowestMarketPrice;
    this.highestMarketPrice = highestMarketPrice;
    this.averageMarketPrice = averageMarketPrice;
    this.numberOfListings = numberOfListings;
  }

  public double getEbayFee() {
    return ebayFee;
  }

  public double getTotalSellingCost() {
    return totalSellingCost;
  }

  public double getNetProfit() {
    return netProfit;
  }

  public double getProfitMargin() {
    return profitMargin;
  }

  public double getLowestMarketPrice() {
    return lowestMarketPrice;
  }

  public double getHighestMarketPrice() {
    return highestMarketPrice;
  }

  public double getAverageMarketPrice() {
    return averageMarketPrice;
  }

  public int getNumberOfListings() {
    return numberOfListings;
  }
}
