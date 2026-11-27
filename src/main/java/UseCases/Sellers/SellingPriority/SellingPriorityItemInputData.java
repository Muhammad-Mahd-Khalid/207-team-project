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

  /** Returns the name of the item. */
  public String getItemName() {
    return itemName;
  }

  /** Returns the condition of the item. */
  public String getCondition() {
    return condition;
  }

  /** Returns the category of the item. */
  public String getCategory() {
    return category;
  }

  /** Returns the ideal price of the item. */
  public BigDecimal getIdealPrice() {
    return idealPrice;
  }

  /** Returns the preparation time of the item. */
  public int getPreparationTime() {
    return preparationTime;
  }
}
