package UseCases.Sellers.SellingPriority;

import java.math.BigDecimal;

/** Stores the input information for one item in the Selling Priority use case. */
public class SellingPriorityItemInputData {

  private final String itemName;
  private final String condition;
  private final String category;
  private final BigDecimal idealPrice;
  private final int preparationTime;

  /** Creates input data for one item. */
  public SellingPriorityItemInputData(
      String itemName,
      String condition,
      String category,
      BigDecimal idealPrice,
      int preparationTime) {
    this.itemName = itemName;
    this.condition = condition;
    this.category = category;
    this.idealPrice = idealPrice;
    this.preparationTime = preparationTime;
  }

  public String getItemName() {
    return itemName;
  }

  public String getCondition() {
    return condition;
  }

  public String getCategory() {
    return category;
  }

  public BigDecimal getIdealPrice() {
    return idealPrice;
  }

  public int getPreparationTime() {
    return preparationTime;
  }
}
