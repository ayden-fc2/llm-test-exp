package mathNode;

import mathNode.Dec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class DecToStringGeneratedTest {

    // Test toString with positive value without parentheses
    @Test
    public void test_toString_positiveValue_noParens() {
        Dec dec = new Dec(5.5) {
            public boolean isParens() { return false; }
        };
        assertEquals("5.5", dec.toString());
    }

    // Test toString with negative value without parentheses
    @Test
    public void test_toString_negativeValue_noParens() {
        Dec dec = new Dec(-3.7) {
            public boolean isParens() { return false; }
        };
        assertEquals("-3.7", dec.toString());
    }

    // Test toString with zero value without parentheses
    @Test
    public void test_toString_zeroValue_noParens() {
        Dec dec = new Dec(0.0) {
            public boolean isParens() { return false; }
        };
        assertEquals("0.0", dec.toString());
    }

    // Test toString with positive value with parentheses
    @Test
    public void test_toString_positiveValue_withParens() {
        Dec dec = new Dec(5.5) {
            public boolean isParens() { return true; }
        };
        assertEquals("(5.5)", dec.toString());
    }

    // Test toString with negative value with parentheses
    @Test
    public void test_toString_negativeValue_withParens() {
        Dec dec = new Dec(-3.7) {
            public boolean isParens() { return true; }
        };
        assertEquals("(-3.7)", dec.toString());
    }

    // Test toString with zero value with parentheses
    @Test
    public void test_toString_zeroValue_withParens() {
        Dec dec = new Dec(0.0) {
            public boolean isParens() { return true; }
        };
        assertEquals("(0.0)", dec.toString());
    }

    // Parameterized test for various double values without parentheses
    @ParameterizedTest
    @ValueSource(doubles = {Double.MIN_VALUE, Double.MAX_VALUE, 1e-10, 1e10, 1.0, -1.0})
    public void test_toString_variousValues_noParens(double value) {
        Dec dec = new Dec(value) {
            public boolean isParens() { return false; }
        };
        String expected = Double.toString(value);
        assertEquals(expected, dec.toString());
    }

    // Parameterized test for various double values with parentheses
    @ParameterizedTest
    @ValueSource(doubles = {Double.MIN_VALUE, Double.MAX_VALUE, 1e-10, 1e10, 1.0, -1.0})
    public void test_toString_variousValues_withParens(double value) {
        Dec dec = new Dec(value) {
            public boolean isParens() { return true; }
        };
        String expected = "(" + Double.toString(value) + ")";
        assertEquals(expected, dec.toString());
    }
}
