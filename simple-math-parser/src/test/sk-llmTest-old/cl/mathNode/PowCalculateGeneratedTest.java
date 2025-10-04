package mathNode;

import mathNode.Pow;
import mathNode.MathNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PowCalculateGeneratedTest {

    // Stub implementation to allow compilation
    static abstract class MathNodeStub extends MathNode {
        private final Number value;
        private final MathNode left;
        private final MathNode right;

        MathNodeStub(Number value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        MathNodeStub(MathNode left, MathNode right) {
            this.value = null;
            this.left = left;
            this.right = right;
        }

        @Override
        public Number calculate() {
            return value != null ? value : 0;
        }

        @Override
        public MathNode getLeftNode() {
            return left;
        }

        @Override
        public MathNode getRightNode() {
            return right;
        }

        @Override
        public String toString() {
            if (value != null) {
                return value.toString();
            }
            if (left != null && right != null) {
                return left.toString() + " ^ " + right.toString();
            }
            return "unknown";
        }
    }

    static class TestPow extends Pow {
        private final MathNode left;
        private final MathNode right;

        TestPow(MathNode left, MathNode right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public MathNode getLeftNode() {
            return left;
        }

        @Override
        public MathNode getRightNode() {
            return right;
        }
    }

    @Test
    void test_calculate_positive_base_positive_exponent() {
        MathNode base = new MathNodeStub(2.0);
        MathNode exponent = new MathNodeStub(3.0);
        Pow pow = new TestPow(base, exponent);
        
        Number result = pow.calculate();
        
        assertEquals(8.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_negative_base_even_exponent() {
        MathNode base = new MathNodeStub(-2.0);
        MathNode exponent = new MathNodeStub(2.0);
        Pow pow = new TestPow(base, exponent);
        
        Number result = pow.calculate();
        
        assertEquals(4.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_negative_base_odd_exponent() {
        MathNode base = new MathNodeStub(-2.0);
        MathNode exponent = new MathNodeStub(3.0);
        Pow pow = new TestPow(base, exponent);
        
        Number result = pow.calculate();
        
        assertEquals(-8.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_zero_base_positive_exponent() {
        MathNode base = new MathNodeStub(0.0);
        MathNode exponent = new MathNodeStub(5.0);
        Pow pow = new TestPow(base, exponent);
        
        Number result = pow.calculate();
        
        assertEquals(0.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_zero_base_zero_exponent() {
        MathNode base = new MathNodeStub(0.0);
        MathNode exponent = new MathNodeStub(0.0);
        Pow pow = new TestPow(base, exponent);
        
        Number result = pow.calculate();
        
        assertEquals(1.0, result.doubleValue(), 1e-9); // 0^0 is conventionally 1
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_one_base_any_exponent() {
        MathNode base = new MathNodeStub(1.0);
        MathNode exponent = new MathNodeStub(100.0);
        Pow pow = new TestPow(base, exponent);
        
        Number result = pow.calculate();
        
        assertEquals(1.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_any_base_zero_exponent() {
        MathNode base = new MathNodeStub(999.0);
        MathNode exponent = new MathNodeStub(0.0);
        Pow pow = new TestPow(base, exponent);
        
        Number result = pow.calculate();
        
        assertEquals(1.0, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_positive_base_negative_exponent() {
        MathNode base = new MathNodeStub(2.0);
        MathNode exponent = new MathNodeStub(-3.0);
        Pow pow = new TestPow(base, exponent);
        
        Number result = pow.calculate();
        
        assertEquals(0.125, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    @Test
    void test_calculate_zero_base_negative_exponent() {
        MathNode base = new MathNodeStub(0.0);
        MathNode exponent = new MathNodeStub(-2.0);
        Pow pow = new TestPow(base, exponent);
        
        assertThrows(ArithmeticException.class, pow::calculate);
    }

    @ParameterizedTest
    @MethodSource("provideEdgeValues")
    void test_calculate_edge_values(Number baseVal, Number expVal, double expected) {
        MathNode base = new MathNodeStub(baseVal);
        MathNode exponent = new MathNodeStub(expVal);
        Pow pow = new TestPow(base, exponent);
        
        Number result = pow.calculate();
        
        assertEquals(expected, result.doubleValue(), 1e-9);
        assertTrue(result instanceof Double);
    }

    static Stream<Arguments> provideEdgeValues() {
        return Stream.of(
            Arguments.of(0.0, 1.0, 0.0),
            Arguments.of(1.0, 0.0, 1.0),
            Arguments.of(-1.0, 2.0, 1.0),
            Arguments.of(-1.0, 3.0, -1.0),
            Arguments.of(2.0, -2.0, 0.25),
            Arguments.of(Double.MAX_VALUE, 0.0, 1.0),
            Arguments.of(Double.MIN_VALUE, 1.0, Double.MIN_VALUE),
            Arguments.of(0.5, 2.0, 0.25),
            Arguments.of(10.0, -1.0, 0.1)
        );
    }

    @Test
    void test_toString_represents_operation() {
        MathNode base = new MathNodeStub(new MathNodeStub(2.0), new MathNodeStub(3.0)) {
            @Override
            public String toString() {
                return "2 ^ 3";
            }
        };
        MathNode exponent = new MathNodeStub(4.0);
        Pow pow = new TestPow(base, exponent);
        
        String result = pow.toString();
        
        assertEquals("2 ^ 3 ^ 4.0", result);
    }
}
