package mathNode;

import mathNode.Dec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class DecToStringGeneratedTest {

    @Test
    public void test_toString_returnsRawValueWhenNoParens() {
        Dec dec = new Dec(5.5) {
            public boolean isParens() { return false; }
        };
        assertEquals("5.5", dec.toString());
    }

    @Test
    public void test_toString_returnsParenthesizedValueWhenParens() {
        Dec dec = new Dec(-3.7) {
            public boolean isParens() { return true; }
        };
        assertEquals("(-3.7)", dec.toString());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.0, 1.0, -1.0, Double.MIN_VALUE, Double.MAX_VALUE, 
                            Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN})
    public void test_toString_handlesSpecialValues(double value) {
        Dec decNoParens = new Dec(value) {
            public boolean isParens() { return false; }
        };
        Dec decWithParens = new Dec(value) {
            public boolean isParens() { return true; }
        };

        String rawStr = Double.toString(value);
        assertEquals(rawStr, decNoParens.toString());
        assertEquals("(" + rawStr + ")", decWithParens.toString());
    }

    @Test
    public void test_toString_handlesVerySmallPositiveDouble() {
        double verySmall = 1e-308;
        Dec dec = new Dec(verySmall) {
            public boolean isParens() { return false; }
        };
        assertTrue(dec.toString().contains("E") || dec.toString().equals("0.0")); // Scientific notation or underflow
    }

    @Test
    public void test_toString_handlesVeryLargePositiveDouble() {
        double veryLarge = 1e308;
        Dec dec = new Dec(veryLarge) {
            public boolean isParens() { return false; }
        };
        assertTrue(dec.toString().contains("E") || dec.toString().equals("Infinity"));
    }

    @Test
    public void test_toString_handlesNegativeZero() {
        Dec dec = new Dec(-0.0) {
            public boolean isParens() { return false; }
        };
        assertEquals("-0.0", dec.toString()); // Java's Double.toString preserves sign of zero
    }
}
