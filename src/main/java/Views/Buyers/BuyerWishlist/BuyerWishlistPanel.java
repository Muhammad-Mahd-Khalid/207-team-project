package Views.Buyers.BuyerWishlist;

import Entities.Item;
import Entities.WishlistEntry;
import InterfaceAdapters.Buyers.BuyerWishlist.BuyerWishlistController;
import InterfaceAdapters.Buyers.BuyerWishlist.BuyerWishlistViewModel;
import UseCases.Buyers.BuyerWishlist.BuyerWishlistOutputData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/** The buyer view supplies its search selection through setSelectedItem. */
public class BuyerWishlistPanel extends JPanel {
  private final BuyerWishlistController controller;
  private final DefaultTableModel rows =
      new DefaultTableModel(new String[] {"Name", "Saved price", "Currency", "eBay link"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
          return false;
        }
      };
  private final JTable table = new JTable(rows);
  private final JLabel message = new JLabel(" ");
  private final JLabel selection = new JLabel("Select an item in the buyer search results.");
  private final JButton add = new JButton("Add to Wishlist");
  private final JButton remove = new JButton("Remove selected item");
  private final JButton refresh = new JButton("Refresh wishlist");
  private final BuyerWishlistViewModel model;
  private WishlistEntry selectedItem;
  private boolean busy;

  /** Creates the panel and connects its buttons to the controller. */
  public BuyerWishlistPanel(BuyerWishlistController controller, BuyerWishlistViewModel model) {
    super(new BorderLayout(8, 8));
    this.controller = controller;
    this.model = model;
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
    actions.add(add);
    actions.add(remove);
    actions.add(refresh);
    JPanel top = new JPanel(new BorderLayout());
    top.add(selection, BorderLayout.NORTH);
    top.add(actions, BorderLayout.SOUTH);
    add(top, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);
    add(message, BorderLayout.SOUTH);
    add.addActionListener(
        event -> {
          WishlistEntry item = selectedItem;
          runAction(() -> controller.addItem(item));
        });
    remove.addActionListener(
        event -> {
          int row = table.getSelectedRow();
          if (row >= 0) {
            String id =
                model
                    .getState()
                    .getItems()
                    .get(table.convertRowIndexToModel(row))
                    .getItem()
                    .getItemId();
            runAction(() -> controller.removeItem(id));
          }
        });
    refresh.addActionListener(event -> refreshWishlist());
    table.getSelectionModel().addListSelectionListener(event -> updateButtons());
    model.addPropertyChangeListener(
        event -> {
          BuyerWishlistOutputData state = (BuyerWishlistOutputData) event.getNewValue();
          if (SwingUtilities.isEventDispatchThread()) {
            render(state);
          } else {
            SwingUtilities.invokeLater(() -> render(state));
          }
        });
    render(model.getState());
  }

  /** Sets a search result and its link. A null item clears the selection. */
  public void setSelectedItem(Item item, String url) {
    selectedItem = item == null ? null : new WishlistEntry(item, url);
    selection.setText(
        item == null
            ? "Select an item in the buyer search results."
            : "Selected: " + item.getTitle());
    updateButtons();
  }

  /** Reloads the saved list when the wishlist is opened. */
  public void refreshWishlist() {
    runAction(controller::viewWishlist);
  }

  private void runAction(Runnable action) {
    if (busy) {
      return;
    }
    busy = true;
    updateButtons();
    new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        action.run();
        return null;
      }

      @Override
      protected void done() {
        try {
          get();
        } catch (Exception ex) {
          message.setForeground(Color.RED);
          message.setText("Could not update wishlist. Please retry.");
        }
        busy = false;
        updateButtons();
      }
    }.execute();
  }

  private void render(BuyerWishlistOutputData state) {
    rows.setRowCount(0);
    for (WishlistEntry entry : state.getItems()) {
      Item item = entry.getItem();
      String price = BigDecimal.valueOf(item.getPrice()).stripTrailingZeros().toPlainString();
      rows.addRow(
          new Object[] {item.getTitle(), price, item.getCurrency(), entry.getListingLink()});
    }
    message.setForeground(state.isSuccess() ? Color.BLACK : Color.RED);
    message.setText(state.getMessage().isEmpty() ? " " : state.getMessage());
    updateButtons();
  }

  private void updateButtons() {
    add.setEnabled(!busy && selectedItem != null);
    remove.setEnabled(!busy && table.getSelectedRow() >= 0);
    refresh.setEnabled(!busy);
  }
}
