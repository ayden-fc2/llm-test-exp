package mathTree;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.LinkedList;

public class MathTreeCleanStrListGeneratedTest {

    // Helper method to invoke the private method using reflection
    private void invokeCleanStrList(LinkedList<String> strList) throws Exception {
        MathTree mathTree = new MathTree();
        java.lang.reflect.Method method = MathTree.class.getDeclaredMethod("cleanStrList", LinkedList.class);
        method.setAccessible(true);
        method.invoke(mathTree, strList);
    }

    @Test
    public void test_cleanStrList_emptyList_noChange() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        invokeCleanStrList(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void test_cleanStrList_singleNegativeNumber_parsedCorrectly() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("-5");
        invokeCleanStrList(list);
        assertEquals(2, list.size());
        assertEquals("-", list.get(0));
        assertEquals("5", list.get(1));
    }

    @Test
    public void test_cleanStrList_negativeAtStart_splitIntoMinusAndNumber() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("-123");
        list.add("+");
        list.add("456");
        invokeCleanStrList(list);
        assertEquals(5, list.size());
        assertEquals("-", list.get(0));
        assertEquals("123", list.get(1));
        assertEquals("+", list.get(2));
        assertEquals("456", list.get(3));
        // Additional element might be added based on implementation, but we check key parts
    }

    @Test
    public void test_cleanStrList_multipleNegatives_allSplitCorrectly() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("-10");
        list.add("*");
        list.add("-20");
        invokeCleanStrList(list);
        assertEquals(5, list.size());
        assertEquals("-", list.get(0));
        assertEquals("10", list.get(1));
        assertEquals("*", list.get(2));
        assertEquals("-", list.get(3));
        assertEquals("20", list.get(4));
    }

    @Test
    public void test_cleanStrList_negativeAfterOperator_handledCorrectly() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("10");
        list.add("+");
        list.add("-5");
        invokeCleanStrList(list);
        assertEquals(4, list.size());
        assertEquals("10", list.get(0));
        assertEquals("+", list.get(1));
        assertEquals("-", list.get(2));
        assertEquals("5", list.get(3));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-", "+", "*", "/", "^"})
    public void test_cleanStrList_operatorsNotModified(String op) throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("3");
        list.add(op);
        list.add("-7");
        invokeCleanStrList(list);
        assertTrue(list.contains(op));
        assertEquals(4, list.size());
        assertEquals("-", list.get(2));
        assertEquals("7", list.get(3));
    }

    @Test
    public void test_cleanStrList_mixedExpression_processedCorrectly() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("-3");
        list.add("*");
        list.add("4");
        list.add("+");
        list.add("-2");
        invokeCleanStrList(list);
        assertEquals(7, list.size());
        assertEquals("-", list.get(0));
        assertEquals("3", list.get(1));
        assertEquals("*", list.get(2));
        assertEquals("4", list.get(3));
        assertEquals("+", list.get(4));
        assertEquals("-", list.get(5));
        assertEquals("2", list.get(6));
    }

    @Test
    public void test_cleanStrList_zeroAsNegative_handled() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("-0");
        invokeCleanStrList(list);
        assertEquals(2, list.size());
        assertEquals("-", list.get(0));
        assertEquals("0", list.get(1));
    }

    @Test
    public void test_cleanStrList_noNegatives_noChanges() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("1");
        list.add("+");
        list.add("2");
        list.add("*");
        list.add("3");
        invokeCleanStrList(list);
        assertEquals(5, list.size());
        assertEquals("1", list.get(0));
        assertEquals("+", list.get(1));
        assertEquals("2", list.get(2));
        assertEquals("*", list.get(3));
        assertEquals("3", list.get(4));
    }

    @Test
    public void test_cleanStrList_negativeOne_splitCorrectly() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("-1");
        invokeCleanStrList(list);
        assertEquals(2, list.size());
        assertEquals("-", list.get(0));
        assertEquals("1", list.get(1));
    }
}
