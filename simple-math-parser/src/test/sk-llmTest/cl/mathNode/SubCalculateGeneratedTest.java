package mathNode;

import mathNode.Sub;
import mathNode.MathNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SubCalculateGeneratedTest {

    // Stub implementation to allow compilation
    static abstract class MathNodeStub implements MathNode {
        private final Number value;
        
        MathNodeStub(Number value) {
            this.value = value;
        }
        
        @Override
        public Number calculate() {
            return value;
        }
    }

    static class SubStub extends Sub {
        private final MathNode left;
        private final MathNode right;
        
        SubStub(MathNode left, MathNode right) {
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
    void test_calculate_both_integers_positive_result() {
        MathNode left = new MathNodeStub(5) {};
        MathNode right = new MathNodeStub(3) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Integer);
        assertEquals(2, result.intValue());
    }

    @Test
    void test_calculate_both_integers_negative_result() {
        MathNode left = new MathNodeStub(3) {};
        MathNode right = new MathNodeStub(5) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Integer);
        assertEquals(-2, result.intValue());
    }

    @Test
    void test_calculate_both_integers_zero_result() {
        MathNode left = new MathNodeStub(5) {};
        MathNode right = new MathNodeStub(5) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Integer);
        assertEquals(0, result.intValue());
    }

    @Test
    void test_calculate_both_integers_negative_minus_negative() {
        MathNode left = new MathNodeStub(-3) {};
        MathNode right = new MathNodeStub(-5) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Integer);
        assertEquals(2, result.intValue());
    }

    @Test
    void test_calculate_integer_minus_large_integer() {
        MathNode left = new MathNodeStub(Integer.MAX_VALUE) {};
        MathNode right = new MathNodeStub(1) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Integer);
        assertEquals(Integer.MAX_VALUE - 1, result.intValue());
    }

    @Test
    void test_calculate_large_negative_integer_minus_positive() {
        MathNode left = new MathNodeStub(Integer.MIN_VALUE) {};
        MathNode right = new MathNodeStub(1) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Integer);
        assertEquals(Integer.MIN_VALUE - 1, result.intValue());
    }

    @Test
    void test_calculate_integer_minus_double_returns_double() {
        MathNode left = new MathNodeStub(5) {};
        MathNode right = new MathNodeStub(3.5) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Double);
        assertEquals(1.5, result.doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_double_minus_integer_returns_double() {
        MathNode left = new MathNodeStub(5.5) {};
        MathNode right = new MathNodeStub(3) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Double);
        assertEquals(2.5, result.doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_double_minus_double_returns_double() {
        MathNode left = new MathNodeStub(5.5) {};
        MathNode right = new MathNodeStub(3.2) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Double);
        assertEquals(2.3, result.doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_zero_minus_zero_both_integers() {
        MathNode left = new MathNodeStub(0) {};
        MathNode right = new MathNodeStub(0) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Integer);
        assertEquals(0, result.intValue());
    }

    @Test
        void test_calculate_zero_minus_zero_double_result() {
        MathNode left = new MathNodeStub(0.0) {};
        MathNode right = new MathNodeStub(0.0) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Double);
        assertEquals(0.0, result.doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_large_double_minus_small_double() {
        MathNode left = new MathNodeStub(1e10) {};
        MathNode right = new MathNodeStub(1e-10) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Double);
        assertEquals(1e10 - 1e-10, result.doubleValue(), 1e-9);
    }

    @ParameterizedTest
    @MethodSource("integerPairsProvider")
    void test_calculate_integer_combinations(IntegerPair pair) {
        MathNode left = new MathNodeStub(pair.left) {};
        MathNode right = new MathNodeStub(pair.right) {};
        Sub sub = new SubStub(left, right);
        
        Number result = sub.calculate();
        
        assertTrue(result instanceof Integer);
        assertEquals(pair.expected, result.intValue());
    }

    static class IntegerPair {
        final int left;
        final int right;
        final int expected;
        
        IntegerPair(int left, int right, int expected) {
            this.left = left;
            this.right = right;
            this.expected = expected;
        }
    }

    static Stream<IntegerPair> integerPairsProvider() {
        return Stream.of(
            new IntegerPair(10, 5, 5),
            new IntegerPair(-10, -5, -5),
            new IntegerPair(0, Integer.MAX_VALUE, -Integer.MAX_VALUE),
            new IntegerPair(Integer.MIN_VALUE, 0, Integer.MIN_VALUE)
        );
    }
}
