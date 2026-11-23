package Entities;

import java.math.BigDecimal;

/** The listing information displayed when a buyer saves an item. */
public class Item {
    private final String id;
    private final String name;
    private final BigDecimal price;
    private final String currency;
    private final String ebayUrl;

    public Item(String id, String name, BigDecimal price, String currency, String ebayUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.ebayUrl = ebayUrl;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCurrency() { return currency; }
    public String getEbayUrl() { return ebayUrl; }
}
