package Views.Buyers.BuyerWishlist;

import DataAccess.JsonBuyerWishlistDataAccessObject;
import Entities.Item;
import InterfaceAdapters.Buyers.BuyerWishlist.*;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistInteractor;
import java.awt.Component;
import java.awt.Container;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class BuyerWishlistPanelTest {
    @Rule public TemporaryFolder temporary = new TemporaryFolder();

    @Test public void buttonsAddDisplayAndRemoveThroughRealStorage() throws Exception {
        Path file = temporary.getRoot().toPath().resolve("wishlist.json");
        BuyerWishlistViewModel model = new BuyerWishlistViewModel();
        BuyerWishlistController controller = new BuyerWishlistController(new BuyerWishlistInteractor(
                new JsonBuyerWishlistDataAccessObject(file), new BuyerWishlistPresenter(model)));
        AtomicReference<BuyerWishlistPanel> ref = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> ref.set(new BuyerWishlistPanel(controller, model)));
        BuyerWishlistPanel panel = ref.get();
        JButton add = findButton(panel, "Add to Wishlist");
        JButton remove = findButton(panel, "Remove selected item");
        JTable table = findTable(panel);
        SwingUtilities.invokeAndWait(() -> {
            assertFalse(add.isEnabled());
            assertFalse(remove.isEnabled());
            panel.setSelectedItem(new Item("123", "Camera", new BigDecimal("99.95"),
                    "CAD", "https://www.ebay.ca/itm/123"));
            assertTrue(add.isEnabled());
        });
        awaitAction(model, add::doClick);
        SwingUtilities.invokeAndWait(() -> {
            assertEquals(1, table.getRowCount());
            assertEquals("Camera", table.getValueAt(0, 0));
            assertEquals("99.95", table.getValueAt(0, 1));
            table.setRowSelectionInterval(0, 0);
        });
        awaitEnabled(remove);
        awaitAction(model, remove::doClick);
        SwingUtilities.invokeAndWait(() -> assertEquals(0, table.getRowCount()));
        assertTrue(new JsonBuyerWishlistDataAccessObject(file).loadItems().isEmpty());
    }

    @Test public void refreshDisplaysPreviouslySavedItems() throws Exception {
        Path file = temporary.getRoot().toPath().resolve("wishlist.json");
        new JsonBuyerWishlistDataAccessObject(file).saveItems(java.util.Collections.singletonList(
                new Item("book", "Book", new BigDecimal("12.00"), "CAD", "https://www.ebay.ca/itm/book")));
        BuyerWishlistViewModel model = new BuyerWishlistViewModel();
        BuyerWishlistController controller = new BuyerWishlistController(new BuyerWishlistInteractor(
                new JsonBuyerWishlistDataAccessObject(file), new BuyerWishlistPresenter(model)));
        AtomicReference<BuyerWishlistPanel> panel = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> panel.set(new BuyerWishlistPanel(controller, model)));
        awaitAction(model, panel.get()::refreshWishlist);
        SwingUtilities.invokeAndWait(() -> {
            JTable table = findTable(panel.get());
            assertEquals("Book", table.getValueAt(0, 0));
            assertEquals("12.00", table.getValueAt(0, 1));
        });
    }

    private void awaitAction(BuyerWishlistViewModel model, Runnable action) throws Exception {
        CountDownLatch changed = new CountDownLatch(1);
        model.addPropertyChangeListener(event -> changed.countDown());
        SwingUtilities.invokeAndWait(action);
        assertTrue("Use case should complete", changed.await(5, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> { });
    }
    private void awaitEnabled(JButton button) throws Exception {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        AtomicReference<Boolean> enabled = new AtomicReference<>(false);
        while (System.nanoTime() < deadline) {
            SwingUtilities.invokeAndWait(() -> enabled.set(button.isEnabled()));
            if (enabled.get()) { return; }
            Thread.sleep(10);
        }
        fail("Button should become enabled after action");
    }
    private JButton findButton(Container parent, String text) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals(text)) {
                return (JButton) component;
            }
            if (component instanceof Container) {
                JButton found = findButton((Container) component, text);
                if (found != null) { return found; }
            }
        }
        return null;
    }
    private JTable findTable(Container parent) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JTable) { return (JTable) component; }
            if (component instanceof Container) {
                JTable found = findTable((Container) component);
                if (found != null) { return found; }
            }
        }
        return null;
    }
}
