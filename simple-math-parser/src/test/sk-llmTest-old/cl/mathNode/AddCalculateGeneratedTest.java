package mathNode;

import mathNode.Add;
import mathNode.MathNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class AddCalculateGeneratedTest {

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
            if (value != null) return value;
            throw new UnsupportedOperationException("Override in subclass");
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

    static class IntegerNode extends MathNodeStub {
        IntegerNode(int value) {
            super(value);
        }

        @Override
        public Number calculate() {
            return getValue();
        }
    }

    static class DoubleNode extends MathNodeStub {
        DoubleNode(double value) {
            super(value);
        }

        @Override
        public Number calculate() {
            return getValue();
        }
    }

    static class AddNode extends Add {
        private final MathNode left;
        private final MathNode right;

        AddNode(MathNode left, MathNode right) {
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

    private static Stream<Object[]> integerAdditionCases() {
        return Stream.of(
            new Object[]{0, 0, 0},
            new Object[]{1, 2, 3},
            new Object[]{-1, -2, -3},
            new Object[]{Integer.MAX_VALUE, 0, Integer.MAX_VALUE},
            new Object[]{Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
            new Object[]{Integer.MAX_VALUE, -1, Integer.MAX_VALUE - 1},
            new Object[]{Integer.MIN_VALUE, 1, Integer.MIN_VALUE + 1}
        );
    }

    @ParameterizedTest
    @MethodSource("integerAdditionCases")
    public void test_calculate_integerAddition(int left, int right, int expected) {
        Add add = new AddNode(new IntegerNode(left), new IntegerNode(right));
        Number result = add.calculate();
        assertInstanceOf(Integer.class, result);
        assertEquals(expected, result.intValue());
    }

    private static Stream<Object[]> mixedTypeAdditionCases() {
        return Stream.of(
            new Object[]{1, 2.5, 3.5},
            new Object[]{2.5, 1, 3.5},
            new Object[]{1.5, 2.5, 4.0},
            new Object[]{0, 0.0, 0.0},
            new Object[]{-1, -2.5, -3.5},
            new Object[]{Integer.MAX_VALUE, 1.0, (double)Integer.MAX_VALUE + 1.0},
            new Object[]{Integer.MIN_VALUE, -1.0, (double)Integer.MIN_VALUE - 1.0}
        );
    }

    @ParameterizedTest
    @MethodSource("mixedTypeAdditionCases")
    public void test_calculate_mixedTypeAddition(Number left, Number right, double expected) {
        Add add = new AddNode(
            left instanceof Integer ? new IntegerNode(left.intValue()) : new DoubleNode(left.doubleValue()),
            right instanceof Integer ? new IntegerNode(right.intValue()) : new DoubleNode(right.doubleValue())
        );
        Number result = add.calculate();
        assertInstanceOf(Double.class, result);
        assertEquals(expected, result.doubleValue(), 1e-9);
    }

    @Test
    public void test_calculate_largeDoubleValues() {
        Add add = new AddNode(new DoubleNode(Double.MAX_VALUE), new DoubleNode(0.0));
        Number result = add.calculate();
        assertInstanceOf(Double.class, result);
        assertEquals(Double.MAX_VALUE, result.doubleValue());

        add = new AddNode(new DoubleNode(-Double.MAX_VALUE), new DoubleNode(0.0));
        result = add.calculate();
        assertInstanceOf(Double.class, result);
        assertEquals(-Double.MAX_VALUE, result.doubleValue());
    }

    @Test
    public void test_calculate_verySmallDoubleValues() {
        Add add = new AddNode(new DoubleNode(Double.MIN_VALUE), new DoubleNode(Double.MIN_VALUE));
        Number result = add.calculate();
        assertInstanceOf(Double.class, result);
        assertEquals(2 * Double.MIN_VALUE, result.doubleValue(), 1e-9);
    }
}
