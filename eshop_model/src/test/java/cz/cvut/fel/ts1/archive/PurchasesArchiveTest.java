package cz.cvut.fel.ts1.archive;

import cz.cvut.fel.ts1.shop.*;
        import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.*;

        import static org.junit.jupiter.api.Assertions.*;

public class PurchasesArchiveTest {

    /* Saving default console PrintStream, since there's no accessible static representation of
     * it once the link to it is lost. */
    private static final PrintStream console = System.out;
    /* Our own OutputStream to redirect the console output for the purpose of testing.
     * ByteArrayOutputStream instance is chosen since it represents the OutputStream and
     * accessible data storage (ByteArray) at the same time.
     * Having a single substitute OutputStream and resetting (clearing) it before every test
     * is slightly more effective than creating a new instance of it before every test, though
     * resetting it is drastically important for the tests results. */
    private static final ByteArrayOutputStream redirectingPrintStream = new ByteArrayOutputStream();

    @Test
    public void printItemPurchaseStatistics_shouldPrintCorrectFormat_whenItemsExist() {
        try {
            PurchasesArchive archive = new PurchasesArchive();
            ShoppingCart cart = new ShoppingCart();
            for (int i = 1; i <= 10; i++) {
                cart.addItem(new StandardItem(i, "testItemName" + i, 9.99f, "testCategory" + (i / 2), 10));
            }
            Order order = new Order(cart, "testCustomer", "testAddress");

            redirectingPrintStream.reset();
            System.setOut(new PrintStream(redirectingPrintStream));

            Map.Entry<List<Order>, Map<Integer, ItemPurchaseArchiveEntry>> archiveSnapshot =
                    processOrderReturnArchiveCopy(archive, order);

            archive.printItemPurchaseStatistics();

            StringBuilder expected = new StringBuilder("ITEM PURCHASE STATISTICS:\n");
            for (ItemPurchaseArchiveEntry entry : archiveSnapshot.getValue().values()) {
                expected.append(entry.toString()).append("\n");
            }

            assertEquals(expected.toString(), redirectingPrintStream.toString().replaceAll("\r", ""));
        } finally {
            System.setOut(console);
        }
    }

    @Test
    public void getHowManyTimesHasBeenItemSold_shouldReturnCorrectCount_whenItemWasOrdered() {
        PurchasesArchive archive = new PurchasesArchive();
        ShoppingCart cart = new ShoppingCart();

        StandardItem item = new StandardItem(42, "CommonItem", 19.99f, "Cat", 10);
        for (int i = 0; i < 5; i++) {
            cart.addItem(item);
        }

        Order order = new Order(cart, "Name", "Address");
        processOrderReturnArchiveCopy(archive, order);

        assertEquals(5, archive.getHowManyTimesHasBeenItemSold(item));
    }

    @Test
    public void getHowManyTimesHasBeenItemSold_shouldReturnZero_whenItemNotOrdered() {
        PurchasesArchive archive = new PurchasesArchive();
        StandardItem item = new StandardItem(99, "NeverSold", 99.99f, "Ghost", 0);

        assertEquals(0, archive.getHowManyTimesHasBeenItemSold(item));
    }

    @Test
    public void putOrderToPurchasesArchive_shouldStoreOrderInArchive() {
        PurchasesArchive archive = new PurchasesArchive();

        Order order = new Order(createCartWithSingleItem(), "Customer", "Address");
        archive.putOrderToPurchasesArchive(order);

        Map.Entry<List<Order>, Map<Integer, ItemPurchaseArchiveEntry>> snapshot =
                processOrderReturnArchiveCopy(archive, null);

        assertTrue(snapshot.getKey().contains(order));
    }

    @Test
    public void putOrderToPurchasesArchive_shouldAccumulateItemCountsCorrectly() {
        PurchasesArchive archive = new PurchasesArchive();

        StandardItem item = new StandardItem(1, "RepeatItem", 10f, "Cat", 10);
        ShoppingCart cart = new ShoppingCart();
        for (int i = 0; i < 3; i++) cart.addItem(item);

        archive.putOrderToPurchasesArchive(new Order(cart, "A", "B"));
        archive.putOrderToPurchasesArchive(new Order(cart, "C", "D"));

        Map.Entry<List<Order>, Map<Integer, ItemPurchaseArchiveEntry>> snapshot =
                processOrderReturnArchiveCopy(archive, null);

        assertEquals(6, snapshot.getValue().get(item.getID()).getCountHowManyTimesHasBeenSold());
    }

    @Test
    public void putOrderToPurchasesArchive_shouldCreateEntriesForNewItems() {
        PurchasesArchive archive = new PurchasesArchive();
        ShoppingCart cart = new ShoppingCart();

        for (int i = 1; i <= 10; i++) {
            cart.addItem(new StandardItem(i, "Item" + i, 10f, "Cat", 5));
        }

        archive.putOrderToPurchasesArchive(new Order(cart, "User", "Addr"));

        Map.Entry<List<Order>, Map<Integer, ItemPurchaseArchiveEntry>> snapshot =
                processOrderReturnArchiveCopy(archive, null);

        for (int i = 1; i <= 10; i++) {
            assertTrue(snapshot.getValue().containsKey(i));
            assertEquals(1, snapshot.getValue().get(i).getCountHowManyTimesHasBeenSold());
        }
    }

    private Map.Entry<List<Order>, Map<Integer, ItemPurchaseArchiveEntry>> processOrderReturnArchiveCopy(PurchasesArchive archive, Order order) {
        try {
            Field orderArchiveField = PurchasesArchive.class.getDeclaredField("orderArchive");
            Field itemArchiveField = PurchasesArchive.class.getDeclaredField("itemPurchaseArchive");

            orderArchiveField.setAccessible(true);
            itemArchiveField.setAccessible(true);

            List<Order> orders = (List<Order>) orderArchiveField.get(archive);
            Map<Integer, ItemPurchaseArchiveEntry> items = (Map<Integer, ItemPurchaseArchiveEntry>) itemArchiveField.get(archive);

            if (order != null) {
                orders.add(order);
                for (Item i : order.getItems()) {
                    items.compute(i.getID(), (id, existing) -> {
                        if (existing == null) return new ItemPurchaseArchiveEntry(i);
                        existing.increaseCountHowManyTimesHasBeenSold(1);
                        return existing;
                    });
                }
            }

            return new AbstractMap.SimpleEntry<>(orders, items);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ShoppingCart createCartWithSingleItem() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new StandardItem(1, "OnlyItem", 12.34f, "Simple", 5));
        return cart;
    }
}
