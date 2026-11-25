package UseCases.Buyers.ItemPricing;

/**
 * Simple interface for the input boundary, implemented by the Use Case Interactor (when I get to it
 * ;_;).
 */
public interface ItemPricingInputBoundary {

  /** Execute the use case from the interface adapter controller. */
  void execute(ItemPricingInputData ItemPricingInputData);
}
