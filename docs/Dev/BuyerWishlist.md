# Buyer Wishlist

The buyer can save a search result, view saved items, and remove an item. The wishlist
is saved to a local JSON file and restored when the app reopens. It uses the team's
`Item` and keeps the original listing URL with each saved item. Duplicate checks use
the item ID.

The interactor loads the list, checks the request, and saves a copy. It shows the new
list after a successful save. A failed write keeps the previous file and list.

## Buyer view integration

Create the panel on the Swing event dispatch thread and add it to the buyer view:

```java
BuyerWishlistPanel wishlist = BuyerWishlistFactory.create(Path.of("data", "buyer-wishlist.json"));
wishlist.setSelectedItem(selectedItem, itemWebUrl);
```

Pass the URL from the search result with the selected item. Call
`setSelectedItem(null, null)` to clear the selection and `refreshWishlist()` when
opening the wishlist tab.

## Run the demo and tests

Use JDK 11 or later and Maven. The demo uses a sample item.

```sh
mvn clean test -Djava.awt.headless=true
mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
java -cp "target/classes:$(cat target/classpath.txt)" Program.BuyerWishlistDemo
```
