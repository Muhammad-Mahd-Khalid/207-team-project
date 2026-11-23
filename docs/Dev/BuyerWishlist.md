# Buyer Wishlist

Jiayou Zhu's Manage Buyer Wishlist use case, based on `Gen/Entities-DA` at `7324c41`.

## Behavior

- Add the currently selected search result and reject duplicate listing IDs.
- Display the saved name, exact price, currency and listing link.
- Remove a selected listing and keep the wishlist after restarting the application.
- Reject incomplete selections. Show an empty state or a useful load/save error.
- Save the displayed price as a snapshot; no API call, price refresh or purchase occurs.
- Keep the previous file and displayed list when a save fails. A corrupt file blocks changes until loading succeeds; it is never silently replaced with an empty wishlist.

The JSON adapter writes a temporary file in the same directory and atomically replaces the saved file. If atomic replacement is unavailable, saving reports failure and keeps the old file. This is a single local buyer profile; concurrent application instances and account login are outside the current scope.

## Existing team code and small shared additions

The team's `BuyerWishlistDataAccessInterface` was empty. It now provides `loadItems` and `saveItems`. `Item` was an empty class; its fields now hold the listing ID, name, BigDecimal price, currency and eBay URL. `UserProfile` holds the local wishlist, following the group discussion. Future search results should construct these shared Items; use the listing ID (not the title) for duplicate detection.

The unfinished API search method at the base revision had no return statement. It now throws `UnsupportedOperationException` until Mahd implements it. The OAuth setup and search responsibility remain with Mahd. Wishlist tests never construct the API object or use credentials.

## Buyer view integration

Create the panel once on the Swing event dispatch thread:

```java
BuyerWishlistPanel wishlist = BuyerWishlistFactory.create(
        Path.of("data", "buyer-wishlist.json"));
```

Add it to the buyer view's tab or card layout. On search selection changes, call:

```java
wishlist.setSelectedItem(selectedItem); // null clears the selection
```

When reopening the wishlist tab, call `wishlist.refreshWishlist()`. The panel already contains add, remove and refresh actions. File access runs in a SwingWorker, and controls are disabled while an operation is pending.

The complete buyer search screen has not been implemented on the shared branch, so this change supplies a reusable panel and factory instead of changing the other member's view.

## Run and test

Requires JDK 11 or later and Maven.

```sh
mvn clean test -Djava.awt.headless=true
mvn package -Djava.awt.headless=true
mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
java -cp "target/classes:$(cat target/classpath.txt)" Program.BuyerWishlistDemo
```

The demo clearly labels its camera as sample data. It saves only to the ignored `data/` directory; an optional first argument chooses another profile JSON path. Test storage uses temporary directories, not the demo profile.

Automated coverage: 13 use case tests, 8 JSON/integration tests, and 2 Swing panel tests. These cover normal add/remove/view, duplicate IDs, invalid input, exact decimal and Unicode round trips, reopen persistence, corrupted files, failed writes, retry, and real button-to-disk integration. Swing tests run headlessly; they verify components and actions, not a manual visual inspection or live eBay integration.
