package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MultCalculateGeneratedTest {

    // Helper stub class to simulate Node behavior
    private static class StubNode implements Node {
        private final Number value;

        StubNode(Number value) {
            this.value = value;
        }

        @Override
        public Number calculate() {
            return value;
        }
    }

    // Helper method to create Mult instance with stub nodes
    private Mult createMult(Number left, Number right) {
        Mult mult = new Mult();
        mult.setLeftNode(new StubNode(left));
        mult.setRightNode(new StubNode(right));
        return mult;
    }

    @Test
    void test_calculate_bothIntegers_returnsIntegerProduct() {
        Mult mult = createMult(6, 7);
        Number result = mult.calculate();
        assertEquals(42, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void test_calculate_bothIntegersZero_returnsZero() {
        Mult mult = createMult(0, 100);
        Number result = mult.calculate();
        assertEquals(0, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void test_calculate_bothIntegersNegative_returnsIntegerProduct() {
        Mult mult = createMult(-5, 3);
        Number result = mult.calculate();
        assertEquals(-15, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void test_calculate_bothIntegersMaxValues_returnsIntegerProduct() {
        Mult mult = createMult(Integer.MAX_VALUE, 1);
        Number result = mult.calculate();
        assertEquals(Integer.MAX_VALUE, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void test_calculate_oneIntegerOneDouble_returnsDoubleProduct() {
        Mult mult = createMult(5, 2.5);
        Number result = mult.calculate();
        assertEquals(12.5, result.doubleValue(), 1e-9);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoubles_returnsDoubleProduct() {
        Mult mult = createMult(3.5, 2.0);
        Number result = mult.calculate();
        assertEquals(7.0, result.doubleValue(), 1e-9);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoublesZero_returnsZero() {
        Mult mult = createMult(0.0, 5.5);
        Number result = mult.calculate();
        assertEquals(0.0, result.doubleValue(), 1e-9);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoublesNegative_returnsDoubleProduct() {
        Mult mult = createMult(-2.5, 3.0);
        Number result = mult.calculate();
        assertEquals(-7.5, result.doubleValue(), 1e-9);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoublesVerySmall_returnsDoubleProduct() {
        Mult mult = createMult(1e-10, 1e-10);
        Number result = mult.calculate();
        assertEquals(1e-20, result.doubleValue(), 1e-30);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoublesVeryLarge_returnsDoubleProduct() {
        Mult mult = createMult(1e10, 1e10);
        Number result = mult.calculate();
        assertEquals(1e20, result.doubleValue(), 1e11);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_integerAndDoubleInfinity_returnsInfinity() {
        Mult mult = createMult(5, Double.POSITIVE_INFINITY);
        Number result = mult.calculate();
        assertTrue(Double.isInfinite(result.doubleValue()));
        assertEquals(Double.POSITIVE_INFINITY, result.doubleValue());
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_integerAndDoubleNaN_returnsNaN() {
        Mult mult = createMult(5, Double.NaN);
        Number result = mult.calculate();
        assertTrue(Double.isNaN(result.doubleValue()));
        assertInstanceOf(Double.class, result);
    }

    @ParameterizedTest
    @MethodSource("provideIntegerPairs")
    void test_calculate_integerMultiplicationCommutative(Number a, Number b) {
        Mult mult1 = createMult(a, b);
        Mult mult2 = createMult(b, a);
        assertEquals(mult1.calculate().doubleValue(), mult2.calculate().doubleValue(), 1e-9);
    }

    static Stream<Arguments> provideIntegerPairs() {
        return Stream.of(
            Arguments.of(3, 4),
            Arguments.of(-2, 5),
            Arguments.of(0, 10),
            Arguments.of(Integer.MAX_VALUE, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDoublePairs")
    void test_calculate_doubleMultiplicationCommutative(Number a, Number b) {
        Mult mult1 = createMult(a, b);
        Mult mult2 = createMult(b, a);
        assertEquals(mult1.calculate().doubleValue(), mult2.calculate().doubleValue(), 1e-9);
    }

    static Stream<Arguments> provideDoublePairs() {
        return Stream.of(
            Arguments.of(2.5, 3.7),
            Arguments.of(-1.5, 4.2),
            Arguments.of(0.0, 9.9),
            Arguments.of(1e10, 1e-10)
        );
    }
}
