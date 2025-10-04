package mathNode;

import mathNode.Int;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IntCalculateGeneratedTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void test_calculate_returnsCorrectIntegerValue(int value) {
        Int node = new Int(value);
        Number result = node.calculate();
        assertEquals(value, result.intValue());
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_withSmallNegativeInteger() {
        Int node = new Int(-1000);
        Number result = node.calculate();
        assertEquals(-1000, result.intValue());
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_withLargePositiveInteger() {
        Int node = new Int(1000000);
        Number result = node.calculate();
        assertEquals(1000000, result.intValue());
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_toString_withoutParens() {
        Int node = new Int(42);
        node.setParens(false);
        assertEquals("42", node.toString());
    }

    @Test
    void test_toString_withParens() {
        Int node = new Int(42);
        node.setParens(true);
        assertEquals("(42)", node.toString());
    }

    @Test
    void test_toString_zeroWithParens() {
        Int node = new Int(0);
        node.setParens(true);
        assertEquals("(0)", node.toString());
    }

    @Test
    void test_toString_negativeWithParens() {
        Int node = new Int(-5);
        node.setParens(true);
        assertEquals("(-5)", node.toString());
    }

    @Test
    void test_toString_maxIntegerWithoutParens() {
        Int node = new Int(Integer.MAX_VALUE);
        node.setParens(false);
        assertEquals(Integer.toString(Integer.MAX_VALUE), node.toString());
    }

    @Test
    void test_toString_minIntegerWithParens() {
        Int node = new Int(Integer.MIN_VALUE);
        node.setParens(true);
        assertEquals("(" + Integer.toString(Integer.MIN_VALUE) + ")", node.toString());
    }
}
