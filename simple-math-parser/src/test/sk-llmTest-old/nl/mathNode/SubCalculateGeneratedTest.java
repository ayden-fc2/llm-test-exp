package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SubCalculateGeneratedTest {

    // Helper stub class to simulate Node behavior
    private static class StubNode implements Node {
        private final Number value;

        public StubNode(Number value) {
            this.value = value;
        }

        @Override
        public Number calculate() {
            return value;
        }
    }

    // Helper method to create a Sub instance with stub nodes
    private Sub createSubWithValues(Number left, Number right) {
        Sub sub = new Sub();
        sub.setLeftNode(new StubNode(left));
        sub.setRightNode(new StubNode(right));
        return sub;
    }

    @Test
    void test_calculate_bothIntegers_positiveResult() {
        Sub sub = createSubWithValues(5, 3);
        Number result = sub.calculate();
        assertEquals(2, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_bothIntegers_negativeResult() {
        Sub sub = createSubWithValues(3, 5);
        Number result = sub.calculate();
        assertEquals(-2, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_bothIntegers_zeroResult() {
        Sub sub = createSubWithValues(5, 5);
        Number result = sub.calculate();
        assertEquals(0, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_bothIntegers_minAndMaxValues() {
        Sub sub = createSubWithValues(Integer.MAX_VALUE, Integer.MIN_VALUE);
        Number result = sub.calculate();
        assertEquals((long) Integer.MAX_VALUE - (long) Integer.MIN_VALUE, result.longValue());
        assertTrue(result instanceof Long || result instanceof Integer); // Could be promoted depending on overflow
    }

    @Test
    void test_calculate_mixedTypes_doubleAndInteger() {
        Sub sub = createSubWithValues(5.5, 2);
        Number result = sub.calculate();
        assertEquals(3.5, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_mixedTypes_integerAndDouble() {
        Sub sub = createSubWithValues(10, 4.5);
        Number result = sub.calculate();
        assertEquals(5.5, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_bothDoubles_positiveResult() {
        Sub sub = createSubWithValues(7.75, 2.25);
        Number result = sub.calculate();
        assertEquals(5.5, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_bothDoubles_negativeResult() {
        Sub sub = createSubWithValues(2.25, 7.75);
        Number result = sub.calculate();
        assertEquals(-5.5, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_bothDoubles_zeroResult() {
        Sub sub = createSubWithValues(3.5, 3.5);
        Number result = sub.calculate();
        assertEquals(0.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_bothDoubles_extremeValues() {
        Sub sub = createSubWithValues(Double.MAX_VALUE, -Double.MAX_VALUE);
        Number result = sub.calculate();
        assertEquals(Double.POSITIVE_INFINITY, result.doubleValue()); // May overflow to infinity
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_withZeroValues_integerSubtractZero() {
        Sub sub = createSubWithValues(42, 0);
        Number result = sub.calculate();
        assertEquals(42, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_withZeroValues_zeroSubtractInteger() {
        Sub sub = createSubWithValues(0, 99);
        Number result = sub.calculate();
        assertEquals(-99, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    void test_calculate_withZeroValues_doubleSubtractZero() {
        Sub sub = createSubWithValues(42.0, 0.0);
        Number result = sub.calculate();
        assertEquals(42.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_withZeroValues_zeroSubtractDouble() {
        Sub sub = createSubWithValues(0.0, 99.0);
        Number result = sub.calculate();
        assertEquals(-99.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @ParameterizedTest
    @MethodSource("provideIntegerPairs")
    void test_calculate_parameterized_integerPairs(Number left, Number right, int expected) {
        Sub sub = createSubWithValues(left, right);
        Number result = sub.calculate();
        assertEquals(expected, result);
        assertTrue(result instanceof Integer);
    }

    static Stream<Arguments> provideIntegerPairs() {
        return Stream.of(
                Arguments.of(10, 5, 5),
                Arguments.of(-5, -3, -2),
                Arguments.of(0, 0, 0),
                Arguments.of(Integer.MIN_VALUE, 0, Integer.MIN_VALUE),
                Arguments.of(0, Integer.MAX_VALUE, -Integer.MAX_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMixedTypePairs")
    void test_calculate_parameterized_mixedTypePairs(Number left, Number right, double expected) {
        Sub sub = createSubWithValues(left, right);
        Number result = sub.calculate();
        assertEquals(expected, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    static Stream<Arguments> provideMixedTypePairs() {
        return Stream.of(
                Arguments.of(5.5, 2, 3.5),
                Arguments.of(10, 4.5, 5.5),
                Arguments.of(-2.5, 3, -5.5),
                Arguments.of(0.1, 0.1, 0.0),
                Arguments.of(Double.MIN_VALUE, 0, Double.MIN_VALUE)
        );
    }
}
