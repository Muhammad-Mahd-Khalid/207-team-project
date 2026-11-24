package UseCases.Buyers.ItemPricing;
import Entities.Item;

public class ItemPricingInteractor implements ItemPricingInputBoundary{

    private final ItemPricingDataAccessInterface itemPricingDataAccessObject;
    private final ItemPricingOutputBoundary buyerPresenter;

    public ItemPricingInteractor(ItemPricingDataAccessInterface itemPricingDataAccessInterface, ItemPricingOutputBoundary buyerOutputBoundary){
        this.itemPricingDataAccessObject = itemPricingDataAccessInterface;
        this.buyerPresenter = buyerOutputBoundary;
    }

    @Override
    public void execute(ItemPricingInputData ItemPricingInputData) {
        final String searchTerm = ItemPricingInputData.getSearchTerm();
    }
}
