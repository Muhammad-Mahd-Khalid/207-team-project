package UseCases.Sellers.SellingPriority;

public interface SellingPriorityOutputBoundary {

    /** Presents the successfully calculated selling priorities. */
    void prepareSuccessView(SellingPriorityOutputData outputData);

    /** Presents an error message when the use case cannot be completed. */
    void prepareFailView(String errorMessage);
}

