package cz.cvut.fel.ts1.shop;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private static ArrayList<Item> validItems;
    private static ShoppingCart validCart;
    private static ShoppingCart cartWithNullItem;
    private static String validCustomerName;
    private static String validCustomerAddress;
    private static int validState;

    @BeforeAll
    public static void setUp() {
        validItems = new ArrayList<>(Arrays.asList(
                new StandardItem(10, "testName1", 9.99f, "testCategory1", 10),
                new StandardItem(11, "testName2", 19.99f, "testCategory1", 20),
                new StandardItem(12, "testName3", 29.99f, "testCategory2", 30)
        ));
        validCart = new ShoppingCart(validItems);
        cartWithNullItem = new ShoppingCart(new ArrayList<>(Arrays.asList(
                new StandardItem(10, "testName1", 9.99f, "testCategory1", 10),
                null
        )));
        validCustomerName = "testCustomerName";
        validCustomerAddress = "testCustomerAddress";
        validState = 10;
    }

    @Test
    public void constructor_withAllValidArguments_initializesCorrectly() {
        Order order = new Order(validCart, validCustomerName, validCustomerAddress, validState);

        assertEquals(validItems, order.getItems());
        assertEquals(validCustomerName, order.getCustomerName());
        assertEquals(validCustomerAddress, order.getCustomerAddress());
        assertEquals(validState, order.getState());
    }

    @Test
    public void constructor_withoutState_setsDefaultStateToZero() {
        Order order = new Order(validCart, validCustomerName, validCustomerAddress);

        assertEquals(validItems, order.getItems());
        assertEquals(validCustomerName, order.getCustomerName());
        assertEquals(validCustomerAddress, order.getCustomerAddress());
        assertEquals(0, order.getState());
    }

    @Test
    public void constructor_withNullCart_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Order(null, validCustomerName, validCustomerAddress, validState);
        });
    }

    @Test
    public void constructor_withNullCustomerName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Order(validCart, null, validCustomerAddress, validState);
        });
    }

    @Test
    public void constructor_withNullCustomerAddress_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Order(validCart, validCustomerName, null, validState);
        });
    }

    @Test
    public void constructor_withCartContainingNullItem_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Order(cartWithNullItem, validCustomerName, validCustomerAddress, validState);
        });
    }

    @Test
    public void constructor_withoutState_withNullCart_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Order(null, validCustomerName, validCustomerAddress);
        });
    }

    @Test
    public void constructor_withoutState_withNullCustomerName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Order(validCart, null, validCustomerAddress);
        });
    }

    @Test
    public void constructor_withoutState_withNullCustomerAddress_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Order(validCart, validCustomerName, null);
        });
    }

    @Test
    public void constructor_withoutState_withCartContainingNullItem_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Order(cartWithNullItem, validCustomerName, validCustomerAddress);
        });
    }
}
