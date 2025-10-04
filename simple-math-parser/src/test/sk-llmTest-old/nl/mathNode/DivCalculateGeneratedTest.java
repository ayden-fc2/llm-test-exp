package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class DivCalculateGeneratedTest {

    // Minimal stub for Node to support testing
    private static abstract class NodeStub implements Node {
        private final Number value;

        protected NodeStub(Number value) {
            this.value = value;
        }

        @Override
        public Number calculate() {
            return value;
        }
    }

    // Test normal division with typical values
    @Test
    void test_calculate_normalDivision() {
        Node left = new NodeStub(10.0) {};
        Node right = new NodeStub(2.0) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertEquals(5.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    // Test division by zero (positive)
    @Test
    void test_calculate_divisionByZeroPositive() {
        Node left = new NodeStub(10.0) {};
        Node right = new NodeStub(0.0) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertTrue(Double.isInfinite(result.doubleValue()));
        assertEquals(Double.POSITIVE_INFINITY, result.doubleValue());
    }

    // Test division by zero (negative)
    @Test
    void test_calculate_divisionByZeroNegative() {
        Node left = new NodeStub(-10.0) {};
        Node right = new NodeStub(0.0) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertTrue(Double.isInfinite(result.doubleValue()));
        assertEquals(Double.NEGATIVE_INFINITY, result.doubleValue());
    }

    // Test 0 divided by a non-zero number
    @Test
    void test_calculate_zeroDividedByNonZero() {
        Node left = new NodeStub(0.0) {};
        Node right = new NodeStub(5.0) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertEquals(0.0, result.doubleValue(), 1e-9);
    }

    // Test negative divided by positive
    @Test
    void test_calculate_negativeDividedByPositive() {
        Node left = new NodeStub(-20.0) {};
        Node right = new NodeStub(4.0) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertEquals(-5.0, result.doubleValue(), 1e-9);
    }

    // Test positive divided by negative
    @Test
    void test_calculate_positiveDividedByNegative() {
        Node left = new NodeStub(20.0) {};
        Node right = new NodeStub(-4.0) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertEquals(-5.0, result.doubleValue(), 1e-9);
    }

    // Test both operands negative
    @Test
    void test_calculate_negativeDividedByNegative() {
        Node left = new NodeStub(-20.0) {};
        Node right = new NodeStub(-4.0) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertEquals(5.0, result.doubleValue(), 1e-9);
    }

    // Test with very large numbers
    @Test
    void test_calculate_veryLargeNumbers() {
        Node left = new NodeStub(Double.MAX_VALUE) {};
        Node right = new NodeStub(Double.MAX_VALUE / 2) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertEquals(2.0, result.doubleValue(), 1e-9);
    }

    // Test with very small numbers
    @Test
    void test_calculate_verySmallNumbers() {
        Node left = new NodeStub(Double.MIN_VALUE * 2) {};
        Node right = new NodeStub(Double.MIN_VALUE) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertEquals(2.0, result.doubleValue(), 1e-9);
    }

    // Parameterized test for various combinations including boundary cases
    @ParameterizedTest
    @CsvSource({
            "1, 1, 1",
            "0, 1, 0",
            "-1, 1, -1",
            "1, -1, -1",
            "-1, -1, 1",
            "7, 3, 2.333333333333333",
            "1000000, 0.000001, 1000000000000.0"
    })
    void test_calculate_parameterized(double leftVal, double rightVal, double expected) {
        Node left = new NodeStub(leftVal) {};
        Node right = new NodeStub(rightVal) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        if (Double.isInfinite(expected)) {
            assertTrue(Double.isInfinite(result.doubleValue()));
            assertEquals(expected, result.doubleValue());
        } else {
            assertEquals(expected, result.doubleValue(), 1e-9);
        }
    }

    // Test with Double.NaN in numerator
    @Test
    void test_calculate_nanNumerator() {
        Node left = new NodeStub(Double.NaN) {};
        Node right = new NodeStub(2.0) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertTrue(Double.isNaN(result.doubleValue()));
    }

    // Test with Double.NaN in denominator
    @Test
    void test_calculate_nanDenominator() {
        Node left = new NodeStub(2.0) {};
        Node right = new NodeStub(Double.NaN) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertTrue(Double.isNaN(result.doubleValue()));
    }

    // Test with infinity in numerator
    @Test
    void test_calculate_infinityNumerator() {
        Node left = new NodeStub(Double.POSITIVE_INFINITY) {};
        Node right = new NodeStub(2.0) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertTrue(Double.isInfinite(result.doubleValue()));
        assertEquals(Double.POSITIVE_INFINITY, result.doubleValue());
    }

    // Test with infinity in denominator
    @Test
    void test_calculate_infinityDenominator() {
        Node left = new NodeStub(2.0) {};
        Node right = new NodeStub(Double.POSITIVE_INFINITY) {};
        Div div = new Div();
        div.setLeftNode(left);
        div.setRightNode(right);

        Number result = div.calculate();
        assertEquals(0.0, result.doubleValue(), 1e-9);
    }
}
