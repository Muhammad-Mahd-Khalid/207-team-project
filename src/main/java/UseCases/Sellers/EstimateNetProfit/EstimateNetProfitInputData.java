package UseCases.Sellers.EstimateNetProfit;

/** Stores the input data required to estimate the net profit of an item. */
public class EstimateNetProfitInputData {

  private final String searchTerm;
  private final double targetSellingPrice;
  private final double buyerShippingCharge;
  private final double sellerShippingCost;
  private final double packagingCost;

  /** Creates input data for the Estimate Net Profit use case. */
  public EstimateNetProfitInputData(
      String searchTerm,
      double targetSellingPrice,
      double buyerShippingCharge,
      double sellerShippingCost,
      double packagingCost) {
    this.searchTerm = searchTerm;
    this.targetSellingPrice = targetSellingPrice;
    this.buyerShippingCharge = buyerShippingCharge;
    this.sellerShippingCost = sellerShippingCost;
    this.packagingCost = packagingCost;
  }

  /** Returns the product search term. */
  public String getSearchTerm() {
    return searchTerm;
  }

  /** Returns the target selling price. */
  public double getTargetSellingPrice() {
    return targetSellingPrice;
  }

  public double getBuyerShippingCharge() {
    return buyerShippingCharge;
  }

  public double getSellerShippingCost() {
    return sellerShippingCost;
  }

  /** Returns the estimated packaging cost. */
  public double getPackagingCost() {
    return packagingCost;
  }
}
