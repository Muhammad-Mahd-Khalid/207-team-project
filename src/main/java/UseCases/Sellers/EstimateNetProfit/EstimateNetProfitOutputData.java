package UseCases.Sellers.EstimateNetProfit;

/** Stores the results of the estimated net profit analysis. */
public class EstimateNetProfitOutputData {

  private final double ebayFee;
  private final double totalSellingCost;
  private final double netProfit;
  private final double profitMargin;

  /** Creates output data for the Estimate Net Profit use case. */
  public EstimateNetProfitOutputData(
      double ebayFee, double totalSellingCost, double netProfit, double profitMargin) {
    this.ebayFee = ebayFee;
    this.totalSellingCost = totalSellingCost;
    this.netProfit = netProfit;
    this.profitMargin = profitMargin;
  }

  /** Returns the estimated eBay fee. */
  public double getEbayFee() {
    return ebayFee;
  }

  /** Returns the total selling cost. */
  public double getTotalSellingCost() {
    return totalSellingCost;
  }

  /** Returns the estimated net profit. */
  public double getNetProfit() {
    return netProfit;
  }

  /** Returns the estimated profit margin. */
  public double getProfitMargin() {
    return profitMargin;
  }
}
