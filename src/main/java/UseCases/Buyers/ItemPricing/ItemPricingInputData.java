package UseCases.Buyers.ItemPricing;

public class ItemPricingInputData {
    private final String searchTerm;

    public ItemPricingInputData(String searchTerm)
    {
        this.searchTerm = searchTerm;
    }

    String getSearchTerm(){
        return this.searchTerm;
    }
}
