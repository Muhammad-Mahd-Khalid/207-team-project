package UseCases.Sellers.SellingPriority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Stores the input information for a batch of items in the Selling Priority use case. */
public class SellingPriorityInputData {

  private final List<SellingPriorityItemInputData> items;

  /** Creates input data for a batch of items. */
  public SellingPriorityInputData(List<SellingPriorityItemInputData> items) {
    Objects.requireNonNull(items, "Items cannot be null");
    this.items = Collections.unmodifiableList(new ArrayList<>(items));
  }

  /** Returns the items in this batch. */
  public List<SellingPriorityItemInputData> getItems() {
    return items;
  }
}
