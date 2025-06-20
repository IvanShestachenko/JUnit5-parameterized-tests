package cz.cvut.fel.ts1.storage;

import cz.cvut.fel.ts1.shop.Item;
import cz.cvut.fel.ts1.shop.StandardItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ItemStockTest {

    @Test
    public void constructor_shouldInitializeItemAndZeroCount() {
        Item item = new StandardItem(10, "testName1", 9.99f, "testCategory1", 10);
        ItemStock itemStock = new ItemStock(item);

        assertEquals(item, itemStock.getItem());
        assertEquals(0, itemStock.getCount());
    }

    @Test
    public void constructor_shouldThrowNullPointerException_whenItemIsNull() {
        assertThrows(NullPointerException.class, () -> new ItemStock(null));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForIncreaseTest")
    public void increaseItemCount_shouldAddToCurrentCount(int initialCount, int numberToIncrease, int expectedCount) {
        ItemStock stock = new ItemStock(new StandardItem(1, "testName1", 9.99f, "testCategory1", 10));
        setInitialCount(stock, initialCount);

        stock.increaseItemCount(numberToIncrease);

        assertEquals(expectedCount, stock.getCount());
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForDecreaseTest")
    public void decreaseItemCount_shouldDecreaseOrThrowIfInsufficient(int initialCount, int numberToDecrease, int expectedCount, boolean expectException) {
        ItemStock stock = new ItemStock(new StandardItem(1, "testName1", 9.99f, "testCategory1", 10));
        setInitialCount(stock, initialCount);

        if (expectException) {
            assertThrows(NoItemInStorage.class, () -> stock.decreaseItemCount(numberToDecrease));
            assertEquals(initialCount, stock.getCount()); // count should remain unchanged
        } else {
            assertDoesNotThrow(() -> stock.decreaseItemCount(numberToDecrease));
            assertEquals(expectedCount, stock.getCount());
        }
    }

    private static Stream<Arguments> provideArgumentsForIncreaseTest() {
        return Stream.of(
                Arguments.of(10, 5, 15),
                Arguments.of(0, 10, 10),
                Arguments.of(10, 0, 10)
        );
    }

    private static Stream<Arguments> provideArgumentsForDecreaseTest() {
        return Stream.of(
                Arguments.of(10, 5, 5, false),
                Arguments.of(0, 10, 0, true),
                Arguments.of(10, 15, 10, true),
                Arguments.of(10, 10, 0, false),
                Arguments.of(10, 0, 10, false)
        );
    }

    private static void setInitialCount(ItemStock stock, int count) {
        try {
            Field field = ItemStock.class.getDeclaredField("count");
            field.setAccessible(true);
            field.setInt(stock, count);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
