package DataAccess;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import Entities.Item;
import Entities.UserProfile;
import Entities.WishlistEntry;
import InterfaceAdapters.Buyers.BuyerWishlist.BuyerWishlistController;
import InterfaceAdapters.Buyers.BuyerWishlist.BuyerWishlistPresenter;
import InterfaceAdapters.Buyers.BuyerWishlist.BuyerWishlistViewModel;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistInteractor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JsonBuyerWishlistDataAccessObjectTest {
  @TempDir Path temporary;

  private Path file() {
    return temporary.resolve("profile/wishlist.json");
  }

  private WishlistEntry item() {
    return new WishlistEntry(
        new Item("v1|123|0", "Camera \"special\" 中文", 123.45, "CAD", "Used", "Cameras"),
        "https://www.ebay.ca/itm/123");
  }

  @Test
  void missingFileMeansEmptyWishlist() throws Exception {
    assertTrue(new JsonBuyerWishlistDataAccessObject(file()).loadItems().isEmpty());
  }

  @Test
  void savesAndReopensEveryField() throws Exception {
    UserProfile profile =
        new UserProfile(UserProfile.UserRole.BUYER, Collections.singletonList(item()));
    new JsonBuyerWishlistDataAccessObject(file()).saveItems(profile.getWishlist());

    UserProfile reopened =
        new UserProfile(
            UserProfile.UserRole.BUYER, new JsonBuyerWishlistDataAccessObject(file()).loadItems());
    WishlistEntry entry = reopened.getWishlist().get(0);
    Item loaded = entry.getItem();
    Item original = item().getItem();
    assertEquals(original.getItemId(), loaded.getItemId());
    assertEquals(original.getTitle(), loaded.getTitle());
    assertEquals(original.getPrice(), loaded.getPrice(), 0.0);
    assertEquals(original.getCurrency(), loaded.getCurrency());
    assertEquals(original.getCondition(), loaded.getCondition());
    assertEquals(original.getCategory(), loaded.getCategory());
    assertEquals(item().getListingLink(), entry.getListingLink());
  }

  @Test
  void readsExistingFilesWithoutConditionOrCategory() throws Exception {
    Files.createDirectories(file().getParent());
    Files.writeString(
        file(),
        "[{\"id\":\"123\",\"name\":\"Camera\",\"price\":\"123.4500\","
            + "\"currency\":\"CAD\",\"ebayUrl\":\"https://www.ebay.ca/itm/123\"}]");
    WishlistEntry entry = new JsonBuyerWishlistDataAccessObject(file()).loadItems().get(0);
    assertEquals("123", entry.getItem().getItemId());
    assertEquals(123.45, entry.getItem().getPrice(), 0.0);
    assertEquals("https://www.ebay.ca/itm/123", entry.getListingLink());
  }

  @Test
  void persistsAnEmptyWishlistAfterRemovingLastItem() throws Exception {
    JsonBuyerWishlistDataAccessObject store = new JsonBuyerWishlistDataAccessObject(file());
    store.saveItems(Collections.singletonList(item()));
    store.saveItems(Collections.emptyList());
    assertTrue(new JsonBuyerWishlistDataAccessObject(file()).loadItems().isEmpty());
  }

  @Test
  void invalidSaveLeavesPreviousBytesIntact() throws Exception {
    JsonBuyerWishlistDataAccessObject store = new JsonBuyerWishlistDataAccessObject(file());
    store.saveItems(Collections.singletonList(item()));
    byte[] previous = Files.readAllBytes(file());
    try {
      store.saveItems(Arrays.asList(item(), item()));
      fail("Duplicate must fail");
    } catch (IOException expected) {
    }
    assertArrayEquals(previous, Files.readAllBytes(file()));
  }

  @Test
  void rejectsMalformedOrIncompleteSavedDataWithoutChangingIt() throws Exception {
    Files.createDirectories(file().getParent());
    for (String corrupt :
        Arrays.asList(
            "",
            "{",
            "{}",
            "null",
            "[{}]",
            "[] garbage",
            "[] []",
            "[{\"id\":1}]",
            "[{\"id\":\"x\",\"name\":\"x\",\"price\":\"NaN\",\"currency\":\"CAD\",\"ebayUrl\":\"https://www.ebay.ca/itm/x\"}]")) {
      Files.writeString(file(), corrupt);
      try {
        new JsonBuyerWishlistDataAccessObject(file()).loadItems();
        fail("Corrupt file must fail");
      } catch (IOException expected) {
      }
      assertEquals(corrupt, Files.readString(file()));
    }
  }

  @Test
  void failedReplacementPreservesTargetAndCleansTemporaryFile() throws Exception {
    Files.createDirectories(file());
    Path marker = file().resolve("keep.txt");
    Files.writeString(marker, "keep");
    try {
      new JsonBuyerWishlistDataAccessObject(file()).saveItems(Collections.singletonList(item()));
      fail("Cannot replace nonempty directory");
    } catch (IOException expected) {
    }
    assertEquals("keep", Files.readString(marker));
    try (java.util.stream.Stream<Path> paths = Files.list(file().getParent())) {
      assertEquals(1, paths.count());
    }
  }

  @Test
  void controllerPresenterAndDiskWorkAcrossRestart() throws Exception {
    BuyerWishlistViewModel first = new BuyerWishlistViewModel();
    BuyerWishlistController controller = controller(first);
    controller.addItem(item());
    assertTrue(first.getState().isSuccess());
    BuyerWishlistViewModel reopened = new BuyerWishlistViewModel();
    BuyerWishlistController second = controller(reopened);
    second.viewWishlist();
    assertEquals(
        item().getItem().getItemId(), reopened.getState().getItems().get(0).getItem().getItemId());
    second.addItem(item());
    assertFalse(reopened.getState().isSuccess());
    second.removeItem(item().getItem().getItemId());
    assertTrue(reopened.getState().isSuccess());
    assertTrue(new JsonBuyerWishlistDataAccessObject(file()).loadItems().isEmpty());
  }

  @Test
  void corruptFileBlocksInteractorSave() throws Exception {
    Files.createDirectories(file().getParent());
    Files.writeString(file(), "broken");
    BuyerWishlistViewModel model = new BuyerWishlistViewModel();
    controller(model).addItem(item());
    assertFalse(model.getState().isSuccess());
    assertEquals("broken", Files.readString(file()));
  }

  private BuyerWishlistController controller(BuyerWishlistViewModel model) {
    return new BuyerWishlistController(
        new BuyerWishlistInteractor(
            new JsonBuyerWishlistDataAccessObject(file()), new BuyerWishlistPresenter(model)));
  }
}
