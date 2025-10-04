package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Dec#toString()}.
 */
class DecToStringGeneratedTest {

    // Minimal stub to allow compilation and testing of Dec.toString()
    private static class Dec {
        private final double value;
        private final boolean parens;

        public Dec(double value, boolean parens) {
            this.value = value;
            this.parens = parens;
        }

        public boolean isParens() {
            return parens;
        }

        public String toString() {
            String str = Double.toString(value);

            if (isParens())
                return '(' + str + ')';
            else
                return str;
        }
    }

    // --- Statement & Branch Coverage Tests ---

    @Test
    void test_toString_withoutParens_returnsRawValue() {
        Dec dec = new Dec(3.14, false);
        assertEquals("3.14", dec.toString());
    }

    @Test
    void test_toString_withParens_returnsParenthesizedValue() {
        Dec dec = new Dec(-2.5, true);
        assertEquals("(-2.5)", dec.toString());
    }

    // --- Boundary and Special Value Tests ---

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.0})
    void test_toString_zeroValues(double val) {
        Dec decNoParens = new Dec(val, false);
        Dec decWithParens = new Dec(val, true);
        String expected = Double.toString(val);
        assertEquals(expected, decNoParens.toString());
        assertEquals("(" + expected + ")", decWithParens.toString());
    }

    @Test
    void test_toString_positiveOne() {
        Dec dec = new Dec(1.0, false);
        assertEquals("1.0", dec.toString());
    }

    @Test
    void test_toString_negativeOne() {
        Dec dec = new Dec(-1.0, true);
        assertEquals("(-1.0)", dec.toString());
    }

    @Test
    void test_toString_maxFiniteDouble() {
        Dec dec = new Dec(Double.MAX_VALUE, false);
        assertEquals("1.7976931348623157E308", dec.toString());
    }

    @Test
    void test_toString_minFiniteDouble() {
        Dec dec = new Dec(-Double.MAX_VALUE, true);
        assertEquals("(-1.7976931348623157E308)", dec.toString());
    }

    @Test
    void test_toString_smallestPositiveNormal() {
        Dec dec = new Dec(Double.MIN_NORMAL, false);
        assertEquals("2.2250738585072014E-308", dec.toString());
    }

    @Test
    void test_toString_smallestPositiveSubnormal() {
        Dec dec = new Dec(Double.MIN_VALUE, true);
        assertEquals("(4.9E-324)", dec.toString());
    }

    @Test
    void test_toString_infinity() {
        Dec dec = new Dec(Double.POSITIVE_INFINITY, false);
        assertEquals("Infinity", dec.toString());
    }

    @Test
    void test_toString_negativeInfinity() {
        Dec dec = new Dec(Double.NEGATIVE_INFINITY, true);
        assertEquals("(-Infinity)", dec.toString());
    }

    @Test
    void test_toString_nan() {
        Dec dec = new Dec(Double.NaN, true);
        assertEquals("(NaN)", dec.toString());
    }

    @Test
    void test_toString_verySmallNumber() {
        Dec dec = new Dec(1e-300, false);
        assertEquals("1.0E-300", dec.toString());
    }

    @Test
    void test_toString_veryLargeNumber() {
        Dec dec = new Dec(9e300, true);
        assertEquals("(9.0E300)", dec.toString());
    }

    @Test
    void test_toString_integerLikeDecimal() {
        Dec dec = new Dec(42.0, false);
        assertEquals("42.0", dec.toString());
    }

    @Test
    void test_toString_fractionalPartOnly() {
        Dec dec = new Dec(0.1, true);
        assertEquals("(0.1)", dec.toString());
    }

    @Test
    void test_toString_negativeFraction() {
        Dec dec = new Dec(-0.001, false);
        assertEquals("-0.001", dec.toString());
    }
}
