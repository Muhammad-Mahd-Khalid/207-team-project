package UseCases.Buyers.ItemPricing;

/** Input Data sent by the controller to the interactor based on view input. */
public class ItemPricingInputData {
  private final String searchTerm;

  /** Construct this input data using a searchTerm. */
  public ItemPricingInputData(String searchTerm) {
    this.searchTerm = searchTerm;
  }

  /** Returns the searchTerm stored in this InputData. */
  String getSearchTerm() {
    return this.searchTerm;
  }
}
