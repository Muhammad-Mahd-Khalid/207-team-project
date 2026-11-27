package UseCases.Sellers.SellingPriority;

/** Output boundary for the Selling Priority use case. */
public interface SellingPriorityOutputBoundary {

  /** Presents the successfully calculated selling priorities. */
  void prepareSuccessView(SellingPriorityOutputData outputData);

  /** Presents an error message when the use case cannot be completed. */
  void prepareFailView(String errorMessage);
}
