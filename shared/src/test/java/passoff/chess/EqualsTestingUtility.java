package passoff.chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Used indirectly to help test the <pre>equals()</pre> and
 * <pre>hashCode()</pre> methods of other classes.
 * <br>
 * This class requires that implementing classes provide a few builder methods,
 * and then it automatically adds multiple tests to the evaluation suite
 * which assert that the <pre>equals()</pre> and <pre>hashCode()</pre> methods function.
 *
 * @param <T> The type to be compared during testing.
 */
public abstract class EqualsTestingUtility<T> {
    private final String className;
    private final String itemsPlural;
//    private T original;
//    private T equivalent;
//    private Collection<T> allDifferent;
    private Map.Entry<String, T> original;
    private Map.Entry<String, T> equivalent;
    private Map<String, T> allDifferent;

    public EqualsTestingUtility(String className, String itemsPlural) {
        this.className = className;
        this.itemsPlural = itemsPlural;
    }

//    protected abstract T buildOriginal();
    protected abstract Map.Entry<String, T> buildOriginal();
//    protected abstract Collection<T> buildAllDifferent();
    protected abstract Map<String, T> buildAllDifferent();


    @BeforeEach
    public void setUp() {
        original = buildOriginal();
        equivalent = buildOriginal(); // For a second time
        allDifferent = buildAllDifferent();
    }

    @Test
    @DisplayName("Equals Testing")
    public void equalsTest() {
        Assertions.assertEquals(original.getValue(), equivalent.getValue(),
                className + ".equals() returned false for equivalent " + itemsPlural);
        for (var different : allDifferent.entrySet()) {
            Assertions.assertNotEquals(original.getValue(), different.getValue(),
                    className + ".equals() returned true for different " + itemsPlural + comparedItemsAsString(original, Set.of(different)));
        }
    }

    @Test
    @DisplayName("HashCode Testing")
    public void hashTest() {
        Assertions.assertEquals(original.getValue().hashCode(), equivalent.getValue().hashCode(),
                className + ".hashCode() returned different values for equivalent " + itemsPlural);
        for (var different : allDifferent.entrySet()) {
            Assertions.assertNotEquals(original.getValue().hashCode(), different.getValue().hashCode(),
                    className + ".hashCode() returned the same value for different " + itemsPlural + comparedItemsAsString(original, Set.of(different)));
        }
    }

    @Test
    @DisplayName("Equals & HashCode Testing")
    public void hashSetTest() {
        Set<T> set = new HashSet<>();
        Set<Map.Entry<String, T>> entries = new HashSet<>();
        set.add(original.getValue());
        entries.add(original);

        // Manually test insertion of original & equal items
        Assertions.assertTrue(set.contains(original.getValue()),
                "[" + className + "] Original item should exist in collection after adding original item");
        Assertions.assertTrue(set.contains(equivalent.getValue()),
                "[" + className + "] Equivalent item should exist in collection after only adding original item");
        Assertions.assertEquals(1, set.size(),
                "[" + className + "] Collection should contain only 1 item after a single insert");
        set.add(equivalent.getValue());
        Assertions.assertEquals(1, set.size(),
                "[" + className + "] Collection should still contain only 1 item after adding equivalent item");

        // Programmatically test insertion of all different items
        int expectedSetSize = 1;
        for (var different : allDifferent.entrySet()) {
            Assertions.assertFalse(set.contains(different.getValue()),
                    "[" + className + "] Different item should not be present in set before insertion" + comparedItemsAsString(different, entries));
            set.add(different.getValue());
            entries.add(different);
            expectedSetSize++;
            Assertions.assertEquals(expectedSetSize, set.size(),
                    "[" + className + "] New item was counted as different during insertion");
        }

    }

    private String comparedItemsAsString(Map.Entry<String, T> itemToCompare, Set<Map.Entry<String, T>> itemsComparedAgainst) {
        return String.format("""

                Comparing %s:
                %s
                against rest of
                %s
                """,
                itemToCompare.getKey(),
                itemToCompare.getValue(),
                setItemsToString(itemsComparedAgainst));
    }

    private String setItemsToString(Set<Map.Entry<String, T>> items) {
        StringBuilder itemsAsString = new StringBuilder();
        for(Map.Entry<String, T> item : items) {
            itemsAsString.append(item.getKey()).append(":\n").append(item.getValue()).append("\n");
        }
        return itemsAsString.toString();
    }

}
