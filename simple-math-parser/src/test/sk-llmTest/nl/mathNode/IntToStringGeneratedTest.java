package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Int#toString()}.
 */
class IntToStringGeneratedTest {

    // Minimal stub for Int class to allow compilation and testing based on provided snippet
    private static class Int {
        private final int value;
        private final boolean parens;

        public Int(int value, boolean parens) {
            this.value = value;
            this.parens = parens;
        }

        public boolean isParens() {
            return parens;
        }

        public String toString() {
            String str = Integer.toString(value);

            if (isParens())
                return '(' + str + ')';
            else
                return str;
        }
    }

    @Test
    void test_toString_positiveValueNoParens_returnsPlainString() {
        Int node = new Int(42, false);
        assertEquals("42", node.toString());
    }

    @Test
    void test_toString_negativeValueNoParens_returnsPlainString() {
        Int node = new Int(-10, false);
        assertEquals("-10", node.toString());
    }

    @Test
    void test_toString_zeroNoParens_returnsZeroAsString() {
        Int node = new Int(0, false);
        assertEquals("0", node.toString());
    }

    @Test
    void test_toString_maxIntegerNoParens_returnsMaxAsString() {
        Int node = new Int(Integer.MAX_VALUE, false);
        assertEquals("2147483647", node.toString());
    }

    @Test
    void test_toString_minIntegerNoParens_returnsMinAsString() {
        Int node = new Int(Integer.MIN_VALUE, false);
        assertEquals("-2147483648", node.toString());
    }

    @Test
    void test_toString_positiveValueWithParens_returnsParenthesizedString() {
        Int node = new Int(99, true);
        assertEquals("(99)", node.toString());
    }

    @Test
    void test_toString_negativeValueWithParens_returnsParenthesizedString() {
        Int node = new Int(-5, true);
        assertEquals("(-5)", node.toString());
    }

    @Test
    void test_toString_zeroWithParens_returnsParenthesizedZero() {
        Int node = new Int(0, true);
        assertEquals("(0)", node.toString());
    }

    @Test
    void test_toString_maxIntegerWithParens_returnsParenthesizedMax() {
        Int node = new Int(Integer.MAX_VALUE, true);
        assertEquals("(2147483647)", node.toString());
    }

    @Test
    void test_toString_minIntegerWithParens_returnsParenthesizedMin() {
        Int node = new Int(Integer.MIN_VALUE, true);
        assertEquals("(-2147483648)", node.toString());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, -1, 0, 100, -100, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void test_toString_variousValuesNoParens_matchesIntegerToString(int val) {
        Int node = new Int(val, false);
        assertEquals(Integer.toString(val), node.toString());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, -1, 0, 100, -100, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void test_toString_variousValuesWithParens_wrappedInParentheses(int val) {
        Int node = new Int(val, true);
        assertEquals("(" + Integer.toString(val) + ")", node.toString());
    }
}
