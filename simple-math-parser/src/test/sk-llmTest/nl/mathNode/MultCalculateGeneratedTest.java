package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MultCalculateGeneratedTest {

    // Stub Node implementation for testing purposes
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

    // Helper method to create Mult instances with stub nodes
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
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_oneIntegerOneDouble_returnsDoubleProduct() {
        Mult mult = createMult(5, 2.5);
        Number result = mult.calculate();
        assertEquals(12.5, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_bothDoubles_returnsDoubleProduct() {
        Mult mult = createMult(3.5, 2.0);
        Number result = mult.calculate();
        assertEquals(7.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_negativeIntegers_returnsCorrectIntegerProduct() {
        Mult mult = createMult(-3, 4);
        Number result = mult.calculate();
        assertEquals(-12, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_zeroMultiplicationWithInteger_returnsZero() {
        Mult mult = createMult(0, 100);
        Number result = mult.calculate();
        assertEquals(0, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_zeroMultiplicationWithDouble_returnsZero() {
        Mult mult = createMult(0.0, 5.5);
        Number result = mult.calculate();
        assertEquals(0.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_multiplicationByOne_returnsSameValueAsOtherOperandInteger() {
        Mult mult = createMult(1, 42);
        Number result = mult.calculate();
        assertEquals(42, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_multiplicationByOne_returnsSameValueAsOtherOperandDouble() {
        Mult mult = createMult(1.0, 42.5);
        Number result = mult.calculate();
        assertEquals(42.5, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_multiplicationOfMaxIntValues_returnsCorrectDoubleProduct() {
        Mult mult = createMult(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Number result = mult.calculate();
        double expected = (double) Integer.MAX_VALUE * Integer.MAX_VALUE;
        assertEquals(expected, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double); // Should be promoted to double due to overflow in int range
    }

    @Test
    void test_calculate_multiplicationOfMinIntValues_returnsCorrectDoubleProduct() {
        Mult mult = createMult(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Number result = mult.calculate();
        double expected = (double) Integer.MIN_VALUE * Integer.MIN_VALUE;
        assertEquals(expected, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_multiplicationOfPositiveAndNegativeInfinity_returnsInfinity() {
        Mult mult = createMult(Double.POSITIVE_INFINITY, -1);
        Number result = mult.calculate();
        assertEquals(Double.NEGATIVE_INFINITY, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_multiplicationOfNaN_returnsNaN() {
        Mult mult = createMult(Double.NaN, 5);
        Number result = mult.calculate();
        assertTrue(Double.isNaN(result.doubleValue()));
        assertTrue(result instanceof Double);
    }

    @ParameterizedTest
    @MethodSource("provideIntegerPairs")
    void test_calculate_integerPairs_returnsCorrectIntegerProduct(int a, int b, int expected) {
        Mult mult = createMult(a, b);
        Number result = mult.calculate();
        assertEquals(expected, result);
        assertTrue(result instanceof Integer);
    }

    static Stream<Arguments> provideIntegerPairs() {
        return Stream.of(
            Arguments.of(0, 0, 0),
            Arguments.of(1, 1, 1),
            Arguments.of(-1, -1, 1),
            Arguments.of(-2, 3, -6),
            Arguments.of(Integer.MAX_VALUE, 0, 0),
            Arguments.of(Integer.MIN_VALUE, 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMixedTypePairs")
    void test_calculate_mixedTypePairs_returnsCorrectDoubleProduct(Number a, Number b, double expected) {
        Mult mult = createMult(a, b);
        Number result = mult.calculate();
        assertEquals(expected, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    static Stream<Arguments> provideMixedTypePairs() {
        return Stream.of(
            Arguments.of(1, 1.5, 1.5),
            Arguments.of(2.5, 2, 5.0),
            Arguments.of(-1, 2.5, -2.5),
            Arguments.of(0.1, 0.2, 0.02),
            Arguments.of(Double.MAX_VALUE, 0.5, Double.MAX_VALUE * 0.5),
            Arguments.of(Double.MIN_VALUE, 2, Double.MIN_VALUE * 2)
        );
    }
}
