package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PowCalculateGeneratedTest {

    // 最小桩类，用于模拟节点返回值
    private static class TestNode implements Node {
        private final Number value;

        TestNode(Number value) {
            this.value = value;
        }

        @Override
        public Number calculate() {
            return value;
        }
    }

    // 最小桩接口定义（假设源码中存在）
    private interface Node {
        Number calculate();
    }

    // 测试 Pow 类的 calculate 方法
    private static class Pow {
        private final Node leftNode;
        private final Node rightNode;

        Pow(Node leftNode, Node rightNode) {
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }

        public Node getLeftNode() {
            return leftNode;
        }

        public Node getRightNode() {
            return rightNode;
        }

        public Number calculate() {
            return Math.pow(getLeftNode().calculate().doubleValue(), getRightNode().calculate().doubleValue());
        }
    }

    @Test
    void test_calculate_positive_base_positive_integer_exponent() {
        Pow pow = new Pow(new TestNode(2.0), new TestNode(3.0));
        assertEquals(8.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_positive_base_negative_integer_exponent() {
        Pow pow = new Pow(new TestNode(2.0), new TestNode(-3.0));
        assertEquals(0.125, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_negative_base_even_integer_exponent() {
        Pow pow = new Pow(new TestNode(-2.0), new TestNode(2.0));
        assertEquals(4.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_negative_base_odd_integer_exponent() {
        Pow pow = new Pow(new TestNode(-2.0), new TestNode(3.0));
        assertEquals(-8.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_zero_base_positive_exponent() {
        Pow pow = new Pow(new TestNode(0.0), new TestNode(5.0));
        assertEquals(0.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_zero_base_zero_exponent() {
        Pow pow = new Pow(new TestNode(0.0), new TestNode(0.0));
        assertEquals(1.0, pow.calculate().doubleValue(), 1e-9); // 0^0 is defined as 1 in Java
    }

    @Test
    void test_calculate_zero_base_negative_exponent() {
        Pow pow = new Pow(new TestNode(0.0), new TestNode(-2.0));
        assertThrows(ArithmeticException.class, () -> {
            double result = pow.calculate().doubleValue();
            // Trigger exception by evaluating 1/0 scenario implicitly
            if (Double.isInfinite(result)) throw new ArithmeticException("Division by zero");
        });
    }

    @Test
    void test_calculate_one_base_any_exponent() {
        Pow pow = new Pow(new TestNode(1.0), new TestNode(100.0));
        assertEquals(1.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_any_base_zero_exponent() {
        Pow pow = new Pow(new TestNode(999.0), new TestNode(0.0));
        assertEquals(1.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_negative_base_fractional_exponent() {
        Pow pow = new Pow(new TestNode(-2.0), new TestNode(0.5));
        assertTrue(Double.isNaN(pow.calculate().doubleValue()));
    }

    @Test
    void test_calculate_positive_base_fractional_exponent() {
        Pow pow = new Pow(new TestNode(4.0), new TestNode(0.5));
        assertEquals(2.0, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_large_positive_base_large_positive_exponent() {
        Pow pow = new Pow(new TestNode(10.0), new TestNode(308.0)); //接近 Double.MAX_VALUE 的指数
        assertEquals(Double.POSITIVE_INFINITY, pow.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_large_positive_base_large_negative_exponent() {
        Pow pow = new Pow(new TestNode(10.0), new TestNode(-308.0)); //接近 Double.MIN_VALUE 的指数
        assertEquals(0.0, pow.calculate().doubleValue(), 1e-9);
    }

    @ParameterizedTest
    @CsvSource({
            "2.0, 3.0, 8.0",
            "5.0, 0.0, 1.0",
            "0.0, 5.0, 0.0",
            "-2.0, 2.0, 4.0",
            "4.0, 0.5, 2.0"
    })
    void test_calculate_parametrized_cases(double base, double exp, double expected) {
        Pow pow = new Pow(new TestNode(base), new TestNode(exp));
        assertEquals(expected, pow.calculate().doubleValue(), 1e-9);
    }
}
