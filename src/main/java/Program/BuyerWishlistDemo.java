package Program;

import Entities.Item;
import Views.Buyers.BuyerWishlist.BuyerWishlistPanel;
import java.awt.BorderLayout;
import java.nio.file.Path;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/** A manual integration demo; this does not perform a live eBay search. */
public final class BuyerWishlistDemo {
  private BuyerWishlistDemo() {}

  /** Opens the wishlist demo with a sample selection. */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          Path file = args.length == 0 ? Path.of("data", "buyer-wishlist.json") : Path.of(args[0]);
          BuyerWishlistPanel panel = BuyerWishlistFactory.create(file);
          panel.setSelectedItem(
              new Item(
                  "demo-camera", "Demo camera (sample data)", 125.50, "CAD", "Used", "Cameras"),
              "https://www.ebay.ca/itm/demo-camera");
          JFrame frame = new JFrame("Buyer Wishlist Demo");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.add(
              new JLabel("Sample selection only. The buyer search view will supply real items."),
              BorderLayout.NORTH);
          frame.add(panel, BorderLayout.CENTER);
          frame.setSize(850, 350);
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
        });
  }
}
