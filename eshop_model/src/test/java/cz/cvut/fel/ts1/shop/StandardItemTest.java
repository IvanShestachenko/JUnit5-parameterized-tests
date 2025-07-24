package cz.cvut.fel.ts1.shop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StandardItemTest {

    @Test
    public void constructor_shouldInitializeFieldsCorrectly_whenValidArgumentsProvided() {
        int id = 10;
        String name = "testName";
        float price = 9.99f;
        String category = "testCategory";
        int loyaltyPoints = 10;

        StandardItem item = new StandardItem(id, name, price, category, loyaltyPoints);

        assertEquals(id, item.getID());
        assertEquals(name, item.getName());
        assertEquals(price, item.getPrice());
        assertEquals(category, item.getCategory());
        assertEquals(loyaltyPoints, item.getLoyaltyPoints());
    }

    @Test
    public void constructor_shouldThrowNullPointerException_whenNameIsNull() {
        assertThrows(NullPointerException.class, () ->
                new StandardItem(10, null, 9.99f, "testCategory", 10));
    }

    @Test
    public void constructor_shouldThrowNullPointerException_whenCategoryIsNull() {
        assertThrows(NullPointerException.class, () ->
                new StandardItem(10, "testName", 9.99f, null, 10));
    }

    @Test
    public void copy_shouldReturnEqualButDistinctObject() {
        StandardItem original = new StandardItem(10, "testName", 9.99f, "testCategory", 10);
        StandardItem copy = original.copy();

        assertNotSame(original, copy);
        assertEquals(original, copy);
    }

    private static Stream<Arguments> provideItemsForEqualsTest() {
        return Stream.of(
                Arguments.of(
                        new StandardItem(10, "testName", 9.99f, "testCategory", 10),
                        new StandardItem(10, "testName", 9.99f, "testCategory", 10),
                        true
                ),
                Arguments.of(
                        new StandardItem(10, "testName", 9.99f, "testCategory", 10),
                        new StandardItem(11, "testName", 9.99f, "testCategory", 10),
                        false
                ),
                Arguments.of(
                        new StandardItem(10, "testName", 9.99f, "testCategory", 10),
                        new StandardItem(10, "differentName", 9.99f, "testCategory", 10),
                        false
                ),
                Arguments.of(
                        new StandardItem(10, "testName", 9.99f, "testCategory", 10),
                        new StandardItem(10, "testName", 10.00f, "testCategory", 10),
                        false
                ),
                Arguments.of(
                        new StandardItem(10, "testName", 9.99f, "testCategory", 10),
                        new StandardItem(10, "testName", 9.99f, "differentCategory", 10),
                        false
                ),
                Arguments.of(
                        new StandardItem(10, "testName", 9.99f, "testCategory", 10),
                        new StandardItem(10, "testName", 9.99f, "testCategory", 11),
                        false
                ),
                Arguments.of(
                        new StandardItem(10, "testName", 9.99f, "testCategory", 10),
                        null,
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideItemsForEqualsTest")
    public void equals_shouldBehaveAsExpected_basedOnFieldComparison(StandardItem item1, StandardItem item2, boolean expected) {
        assertEquals(expected, item1.equals(item2));
    }
}
