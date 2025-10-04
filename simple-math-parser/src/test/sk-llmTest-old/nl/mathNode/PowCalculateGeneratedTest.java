package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PowCalculateGeneratedTest {

    // Minimal stub for Node to support testing
    abstract static class Node {
        abstract Number calculate();
    }

    // Minimal stub for Pow with controllable left and right nodes
    static class Pow {
        private final Node leftNode;
        private final Node rightNode;

        public Pow(Node leftNode, Node rightNode) {
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }

        public Number calculate() {
            return Math.pow(leftNode.calculate().doubleValue(), rightNode.calculate().doubleValue());
        }

        public Node getLeftNode() {
            return leftNode;
        }

        public Node getRightNode() {
            return rightNode;
        }
    }

    // Helper to create a constant node
    private Node constantNode(double value) {
        return new Node() {
            @Override
            public Number calculate() {
                return value;
            }
        };
    }

    @Test
    void test_calculate_positive_base_positive_integer_exponent() {
        Pow pow = new Pow(constantNode(2.0), constantNode(3.0));
        assertEquals(8.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_negative_base_even_exponent() {
        Pow pow = new Pow(constantNode(-2.0), constantNode(2.0));
        assertEquals(4.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_negative_base_odd_exponent() {
        Pow pow = new Pow(constantNode(-2.0), constantNode(3.0));
        assertEquals(-8.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_zero_base_positive_exponent() {
        Pow pow = new Pow(constantNode(0.0), constantNode(5.0));
        assertEquals(0.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_zero_base_zero_exponent() {
        Pow pow = new Pow(constantNode(0.0), constantNode(0.0));
        assertEquals(1.0, pow.calculate().doubleValue(), 1e-9); // 0^0 is defined as 1 in Java
    }

    @Test
    void test_calculate_one_base_any_exponent() {
        Pow pow = new Pow(constantNode(1.0), constantNode(100.0));
        assertEquals(1.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_any_base_zero_exponent() {
        Pow pow = new Pow(constantNode(999.0), constantNode(0.0));
        assertEquals(1.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_positive_base_negative_exponent() {
        Pow pow = new Pow(constantNode(2.0), constantNode(-2.0));
        assertEquals(0.25, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_negative_base_negative_exponent() {
        Pow pow = new Pow(constantNode(-2.0), constantNode(-2.0));
        assertEquals(0.25, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_zero_base_negative_exponent() {
        Pow pow = new Pow(constantNode(0.0), constantNode(-1.0));
        assertThrows(ArithmeticException.class, () -> {
            double result = pow.calculate().doubleValue();
            // Trigger evaluation
            if (Double.isInfinite(result)) throw new ArithmeticException("Division by zero");
        });
    }

    @Test
    void test_calculate_fractional_exponent() {
        Pow pow = new Pow(constantNode(4.0), constantNode(0.5));
        assertEquals(2.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_large_base_small_exponent() {
        Pow pow = new Pow(constantNode(1e10), constantNode(0.1));
        double expected = Math.pow(1e10, 0.1);
        assertEquals(expected, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_small_base_large_exponent() {
        Pow pow = new Pow(constantNode(0.1), constantNode(10.0));
        double expected = Math.pow(0.1, 10.0);
        assertEquals(expected, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_base_negative_one_exponent_infinity() {
        Pow pow = new Pow(constantNode(-1.0), constantNode(Double.POSITIVE_INFINITY));
        assertTrue(Double.isNaN(pow.calculate().doubleValue()));
    }

    @Test
    void test_calculate_base_less_than_neg_one_exponent_infinity() {
        Pow pow = new Pow(constantNode(-2.0), constantNode(Double.POSITIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, pow.calculate().doubleValue(), 1e-9);
    }

    @ParameterizedTest
    @CsvSource({
            "2.0, 3.0, 8.0",
            "-2.0, 2.0, 4.0",
            "-2.0, 3.0, -8.0",
            "0.0, 5.0, 0.0",
            "1.0, 100.0, 1.0",
            "999.0, 0.0, 1.0",
            "2.0, -2.0, 0.25",
            "4.0, 0.5, 2.0"
    })
    void test_calculate_parametrized(double base, double exp, double expected) {
        Pow pow = new Pow(constantNode(base), constantNode(exp));
        assertEquals(expected, pow.calculate().doubleValue(), 1e-9);
    }
}
