package UseCases.Buyers.BuyerWishlist;

import Entities.Item;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BuyerWishlistInteractorTest {
    private Store store;
    private BuyerWishlistInteractor interactor;
    private BuyerWishlistOutputData result;
    private Item camera;

    @Before public void setUp() {
        store = new Store();
        interactor = new BuyerWishlistInteractor(store, output -> result = output);
        camera = item("camera", "Camera", "125.50");
    }

    @Test public void emptyWishlistHasExpectedMessage() {
        interactor.viewWishlist();
        assertTrue(result.isSuccess());
        assertEquals("Your wishlist is empty", result.getMessage());
    }
    @Test public void savesSelectedListingSnapshot() {
        interactor.addItem(camera);
        assertTrue(result.isSuccess());
        assertEquals(1, store.saved.size());
        assertSame(camera, store.saved.get(0));
        assertEquals(new BigDecimal("125.50"), result.getItems().get(0).getPrice());
    }
    @Test public void duplicateIdDoesNotReplaceOriginalPriceOrSaveAgain() {
        interactor.addItem(camera);
        interactor.addItem(item("camera", "Changed name", "999.00"));
        assertFalse(result.isSuccess());
        assertEquals(1, store.saves);
        assertSame(camera, store.saved.get(0));
        assertTrue(result.getMessage().contains("already saved"));
    }
    @Test public void differentIdsWithSameNameAreSeparateListings() {
        interactor.addItem(camera);
        interactor.addItem(item("other", "Camera", "125.50"));
        assertEquals(2, result.getItems().size());
    }
    @Test public void rejectsMissingAndInvalidInformationWithoutWriting() {
        List<Item> invalid = Arrays.asList(null,
                item(" ", "Camera", "1"), item("id", "", "1"),
                new Item("id", "Camera", null, "CAD", "https://www.ebay.ca/itm/1"),
                item("id", "Camera", "-1"),
                new Item("id", "Camera", BigDecimal.ONE, "", "https://www.ebay.ca/itm/1"),
                new Item("id", "Camera", BigDecimal.ONE, "CAD", null),
                new Item("id", "Camera", BigDecimal.ONE, "CAD", "javascript:alert(1)"),
                new Item("id", "Camera", BigDecimal.ONE, "CAD", "bad url"));
        for (Item item : invalid) {
            interactor.addItem(item);
            assertFalse(result.isSuccess());
        }
        assertEquals(0, store.saves);
    }
    @Test public void zeroPriceIsAllowed() {
        interactor.addItem(item("free", "Free item", "0"));
        assertTrue(result.isSuccess());
    }
    @Test public void removesOnlySelectedListing() {
        interactor.addItem(camera);
        interactor.addItem(item("other", "Book", "10"));
        interactor.removeItem("camera");
        assertTrue(result.isSuccess());
        assertEquals("other", store.saved.get(0).getId());
        assertEquals(1, store.saved.size());
    }
    @Test public void missingRemovalDoesNotWrite() {
        interactor.removeItem(null);
        assertFalse(result.isSuccess());
        interactor.removeItem("absent");
        assertFalse(result.isSuccess());
        assertEquals(0, store.saves);
    }
    @Test public void failedAddKeepsPreviousStateAndCanRetry() {
        interactor.addItem(camera);
        store.failSave = true;
        interactor.addItem(item("other", "Book", "10"));
        assertFalse(result.isSuccess());
        assertEquals(1, result.getItems().size());
        assertEquals(1, store.saved.size());
        store.failSave = false;
        interactor.addItem(item("other", "Book", "10"));
        assertTrue(result.isSuccess());
        assertEquals(2, store.saved.size());
    }
    @Test public void failedRemovalKeepsPreviousState() {
        interactor.addItem(camera);
        store.failSave = true;
        interactor.removeItem("camera");
        assertFalse(result.isSuccess());
        assertEquals(1, result.getItems().size());
        assertEquals(1, store.saved.size());
    }
    @Test public void loadFailureBlocksBothMutationsAndRetainsLastDisplay() {
        interactor.addItem(camera);
        store.failLoad = true;
        interactor.viewWishlist();
        assertFalse(result.isSuccess());
        assertEquals(1, result.getItems().size());
        interactor.addItem(item("other", "Book", "1"));
        interactor.removeItem("camera");
        assertFalse(result.isSuccess());
        assertEquals(1, store.saves);
    }
    @Test public void readsLatestStoredWishlistBeforeChangingIt() {
        store.saved.add(camera);
        interactor.addItem(item("other", "Book", "10"));
        assertEquals(2, store.saved.size());
    }
    @Test public void outputCannotBeModifiedByView() {
        interactor.addItem(camera);
        try { result.getItems().clear(); fail("Expected immutable list"); }
        catch (UnsupportedOperationException expected) { }
        assertEquals(1, store.saved.size());
    }
    private Item item(String id, String name, String price) {
        return new Item(id, name, new BigDecimal(price), "CAD", "https://www.ebay.ca/itm/" + id);
    }
    private static class Store implements BuyerWishlistDataAccessInterface {
        List<Item> saved = new ArrayList<>();
        int saves;
        boolean failLoad;
        boolean failSave;
        public List<Item> loadItems() throws IOException {
            if (failLoad) { throw new IOException("load failed"); }
            return new ArrayList<>(saved);
        }
        public void saveItems(List<Item> items) throws IOException {
            if (failSave) { throw new IOException("save failed"); }
            saved = new ArrayList<>(items);
            saves++;
        }
    }
}
