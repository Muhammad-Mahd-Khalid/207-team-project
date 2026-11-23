package DataAccess;

import Entities.Item;
import InterfaceAdapters.Buyers.BuyerWishlist.*;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistInteractor;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class JsonBuyerWishlistDataAccessObjectTest {
    @Rule public TemporaryFolder temporary = new TemporaryFolder();
    private Path file() { return temporary.getRoot().toPath().resolve("profile/wishlist.json"); }
    private Item item() {
        return new Item("v1|123|0", "Camera \"special\" 中文", new BigDecimal("123.4500"),
                "CAD", "https://www.ebay.ca/itm/123");
    }
    @Test public void missingFileMeansEmptyWishlist() throws Exception {
        assertTrue(new JsonBuyerWishlistDataAccessObject(file()).loadItems().isEmpty());
    }
    @Test public void savesAndReopensEveryFieldWithoutLosingDecimalPrecision() throws Exception {
        new JsonBuyerWishlistDataAccessObject(file()).saveItems(Collections.singletonList(item()));
        Item loaded = new JsonBuyerWishlistDataAccessObject(file()).loadItems().get(0);
        assertEquals(item().getId(), loaded.getId());
        assertEquals(item().getName(), loaded.getName());
        assertEquals(item().getPrice(), loaded.getPrice());
        assertEquals(item().getCurrency(), loaded.getCurrency());
        assertEquals(item().getEbayUrl(), loaded.getEbayUrl());
    }
    @Test public void persistsAnEmptyWishlistAfterRemovingLastItem() throws Exception {
        JsonBuyerWishlistDataAccessObject store = new JsonBuyerWishlistDataAccessObject(file());
        store.saveItems(Collections.singletonList(item()));
        store.saveItems(Collections.emptyList());
        assertTrue(new JsonBuyerWishlistDataAccessObject(file()).loadItems().isEmpty());
    }
    @Test public void invalidSaveLeavesPreviousBytesIntact() throws Exception {
        JsonBuyerWishlistDataAccessObject store = new JsonBuyerWishlistDataAccessObject(file());
        store.saveItems(Collections.singletonList(item()));
        byte[] previous = Files.readAllBytes(file());
        try { store.saveItems(Arrays.asList(item(), item())); fail("Duplicate must fail"); }
        catch (IOException expected) { }
        assertArrayEquals(previous, Files.readAllBytes(file()));
    }
    @Test public void rejectsMalformedOrIncompleteSavedDataWithoutChangingIt() throws Exception {
        Files.createDirectories(file().getParent());
        for (String corrupt : Arrays.asList("", "{", "{}", "null", "[{}]", "[] garbage", "[] []",
                "[{\"id\":1}]", "[{\"id\":\"x\",\"name\":\"x\",\"price\":\"NaN\",\"currency\":\"CAD\",\"ebayUrl\":\"https://www.ebay.ca/itm/x\"}]")) {
            Files.writeString(file(), corrupt);
            try { new JsonBuyerWishlistDataAccessObject(file()).loadItems(); fail("Corrupt file must fail"); }
            catch (IOException expected) { }
            assertEquals(corrupt, Files.readString(file()));
        }
    }
    @Test public void failedReplacementPreservesTargetAndCleansTemporaryFile() throws Exception {
        Files.createDirectories(file());
        Path marker = file().resolve("keep.txt");
        Files.writeString(marker, "keep");
        try {
            new JsonBuyerWishlistDataAccessObject(file()).saveItems(Collections.singletonList(item()));
            fail("Cannot replace nonempty directory");
        } catch (IOException expected) { }
        assertEquals("keep", Files.readString(marker));
        try (java.util.stream.Stream<Path> paths = Files.list(file().getParent())) {
            assertEquals(1, paths.count());
        }
    }
    @Test public void controllerPresenterAndDiskWorkAcrossRestart() throws Exception {
        BuyerWishlistViewModel first = new BuyerWishlistViewModel();
        BuyerWishlistController controller = controller(first);
        controller.addItem(item());
        assertTrue(first.getState().isSuccess());
        BuyerWishlistViewModel reopened = new BuyerWishlistViewModel();
        BuyerWishlistController second = controller(reopened);
        second.viewWishlist();
        assertEquals(item().getId(), reopened.getState().getItems().get(0).getId());
        second.addItem(item());
        assertFalse(reopened.getState().isSuccess());
        second.removeItem(item().getId());
        assertTrue(reopened.getState().isSuccess());
        assertTrue(new JsonBuyerWishlistDataAccessObject(file()).loadItems().isEmpty());
    }
    @Test public void corruptFileBlocksInteractorSave() throws Exception {
        Files.createDirectories(file().getParent());
        Files.writeString(file(), "broken");
        BuyerWishlistViewModel model = new BuyerWishlistViewModel();
        controller(model).addItem(item());
        assertFalse(model.getState().isSuccess());
        assertEquals("broken", Files.readString(file()));
    }
    private BuyerWishlistController controller(BuyerWishlistViewModel model) {
        return new BuyerWishlistController(new BuyerWishlistInteractor(
                new JsonBuyerWishlistDataAccessObject(file()), new BuyerWishlistPresenter(model)));
    }
}
