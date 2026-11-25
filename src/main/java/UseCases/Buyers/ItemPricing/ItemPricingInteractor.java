package UseCases.Buyers.ItemPricing;

/**
 * Use Case Interactor for the Item Pricing Use Case. Takes the InputData object, requests entities
 * from the DAO, and then performs statistical trimming + analysis on the entities to send to the
 * interface adapters.
 */
public class ItemPricingInteractor implements ItemPricingInputBoundary {

  private final ItemPricingDataAccessInterface itemPricingDataAccessObject;
  private final ItemPricingOutputBoundary buyerPresenter;

  /** Create a new instance of the ItemPricingIteractor. */
  public ItemPricingInteractor(
      ItemPricingDataAccessInterface itemPricingDataAccessInterface,
      ItemPricingOutputBoundary buyerOutputBoundary) {
    this.itemPricingDataAccessObject = itemPricingDataAccessInterface;
    this.buyerPresenter = buyerOutputBoundary;
  }

  /** Execute the core logic for this use case. */
  @Override
  public void execute(ItemPricingInputData ItemPricingInputData) {
    final String searchTerm = ItemPricingInputData.getSearchTerm();
  }
}
