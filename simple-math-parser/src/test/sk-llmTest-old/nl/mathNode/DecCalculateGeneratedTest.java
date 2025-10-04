package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class DecCalculateGeneratedTest {

    // Helper to set private 'value' field for testing purposes
    private Dec createDecWithPrivateValue(Number value) {
        Dec dec = new Dec();
        try {
            Field valueField = Dec.class.getDeclaredField("value");
            valueField.setAccessible(true);
            valueField.set(dec, value);
        } catch (Exception e) {
            fail("Failed to set private 'value' field", e);
        }
        return dec;
    }

    @Test
    void test_calculate_returnsStoredIntegerValue() {
        int inputValue = 42;
        Dec dec = createDecWithPrivateValue(inputValue);
        Number result = dec.calculate();
        assertEquals(inputValue, result.intValue());
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_returnsStoredDoubleValue() {
        double inputValue = 3.14159;
        Dec dec = createDecWithPrivateValue(inputValue);
        Number result = dec.calculate();
        assertEquals(inputValue, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_returnsZero() {
        Dec dec = createDecWithPrivateValue(0);
        Number result = dec.calculate();
        assertEquals(0, result.intValue());
    }

    @Test
    void test_calculate_returnsNegativeOne() {
        Dec dec = createDecWithPrivateValue(-1);
        Number result = dec.calculate();
        assertEquals(-1, result.intValue());
    }

    @Test
    void test_calculate_returnsMaxInteger() {
        Dec dec = createDecWithPrivateValue(Integer.MAX_VALUE);
        Number result = dec.calculate();
        assertEquals(Integer.MAX_VALUE, result.intValue());
    }

    @Test
    void test_calculate_returnsMinInteger() {
        Dec dec = createDecWithPrivateValue(Integer.MIN_VALUE);
        Number result = dec.calculate();
        assertEquals(Integer.MIN_VALUE, result.intValue());
    }

    @Test
    void test_calculate_returnsVeryLargeDouble() {
        double veryLarge = 1e308;
        Dec dec = createDecWithPrivateValue(veryLarge);
        Number result = dec.calculate();
        assertEquals(veryLarge, result.doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_returnsVerySmallDouble() {
        double verySmall = 1e-308;
        Dec dec = createDecWithPrivateValue(verySmall);
        Number result = dec.calculate();
        assertEquals(verySmall, result.doubleValue(), 1e-9);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void test_calculate_handlesSpecialFloatingPointValues(double specialValue) {
        Dec dec = createDecWithPrivateValue(specialValue);
        Number result = dec.calculate();
        if (Double.isNaN(specialValue)) {
            assertTrue(Double.isNaN(result.doubleValue()));
        } else if (Double.isInfinite(specialValue)) {
            assertTrue(Double.isInfinite(result.doubleValue()));
            assertEquals(specialValue > 0, result.doubleValue() > 0);
        }
    }
}
