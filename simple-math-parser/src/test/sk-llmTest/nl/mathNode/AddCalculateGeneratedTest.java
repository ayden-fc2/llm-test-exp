package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AddCalculateGeneratedTest {

    // Mock Node implementation for testing
    private static class MockNode implements Node {
        private final Number value;

        MockNode(Number value) {
            this.value = value;
        }

        @Override
        public Number calculate() {
            return value;
        }
    }

    // Helper to create Add node with mock children
    private Add createAddNode(Number left, Number right) {
        Add add = new Add();
        add.setLeftNode(new MockNode(left));
        add.setRightNode(new MockNode(right));
        return add;
    }

    @Test
    void test_calculate_bothIntegers_returnsIntegerSum() {
        Add add = createAddNode(2, 3);
        Number result = add.calculate();
        assertEquals(5, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void test_calculate_bothIntegers_negativeValues_returnsCorrectSum() {
        Add add = createAddNode(-2, -3);
        Number result = add.calculate();
        assertEquals(-5, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void test_calculate_bothIntegers_zeroValues_returnsZero() {
        Add add = createAddNode(0, 0);
        Number result = add.calculate();
        assertEquals(0, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void test_calculate_bothIntegers_maxIntegerValues_returnsCorrectSum() {
        Add add = createAddNode(Integer.MAX_VALUE, -1);
        Number result = add.calculate();
        assertEquals(Integer.MAX_VALUE - 1, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void test_calculate_bothIntegers_minIntegerValues_returnsCorrectSum() {
        Add add = createAddNode(Integer.MIN_VALUE, 1);
        Number result = add.calculate();
        assertEquals(Integer.MIN_VALUE + 1, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void test_calculate_oneDoubleOneInteger_returnsDoubleSum() {
        Add add = createAddNode(2.5, 3);
        Number result = add.calculate();
        assertEquals(5.5, result.doubleValue(), 1e-9);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoubles_returnsDoubleSum() {
        Add add = createAddNode(2.5, 3.7);
        Number result = add.calculate();
        assertEquals(6.2, result.doubleValue(), 1e-9);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoubles_negativeValues_returnsCorrectSum() {
        Add add = createAddNode(-2.5, -3.7);
        Number result = add.calculate();
        assertEquals(-6.2, result.doubleValue(), 1e-9);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoubles_zeroValues_returnsZero() {
        Add add = createAddNode(0.0, 0.0);
        Number result = add.calculate();
        assertEquals(0.0, result.doubleValue(), 1e-9);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoubles_extremeValues_returnsInfinity() {
        Add add = createAddNode(Double.MAX_VALUE, Double.MAX_VALUE);
        Number result = add.calculate();
        assertTrue(Double.isInfinite(result.doubleValue()));
        assertEquals(Double.POSITIVE_INFINITY, result.doubleValue());
        assertInstanceOf(Double.class, result);
    }

    @Test
    void test_calculate_bothDoubles_verySmall_returnsCorrectSum() {
        Add add = createAddNode(1e-10, 2e-10);
        Number result = add.calculate();
        assertEquals(3e-10, result.doubleValue(), 1e-15);
        assertInstanceOf(Double.class, result);
    }

    @ParameterizedTest
    @MethodSource("integerPairs")
    void test_calculate_integerCombinations_coverBranches(Number left, Number right, Number expected) {
        Add add = createAddNode(left, right);
        Number result = add.calculate();
        assertEquals(expected, result);
        assertInstanceOf(Integer.class, result);
    }

    static Stream<Arguments> integerPairs() {
        return Stream.of(
            Arguments.of(1, 2, 3),
            Arguments.of(-1, -2, -3),
            Arguments.of(0, 5, 5),
            Arguments.of(Integer.MAX_VALUE, 0, Integer.MAX_VALUE),
            Arguments.of(Integer.MIN_VALUE, 0, Integer.MIN_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource("mixedNumberPairs")
    void test_calculate_mixedNumberCombinations_coverDecimalBranch(Number left, Number right, double expected) {
        Add add = createAddNode(left, right);
        Number result = add.calculate();
        assertEquals(expected, result.doubleValue(), 1e-9);
        assertInstanceOf(Double.class, result);
    }

    static Stream<Arguments> mixedNumberPairs() {
        return Stream.of(
            Arguments.of(1.5, 2, 3.5),
            Arguments.of(2, 1.5, 3.5),
            Arguments.of(1.1, 2.2, 3.3),
            Arguments.of(-1.5, -2.5, -4.0),
            Arguments.of(0.0, 0, 0.0)
        );
    }
}
