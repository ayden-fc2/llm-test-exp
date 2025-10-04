package mathTree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MathTreeSolveGeneratedTest {

    // Stub class to simulate a calculable node returning a fixed value
    private static class ValueNode implements Node {
        private final Number value;

        ValueNode(Number value) {
            this.value = value;
        }

        @Override
        public Number calculate() {
            return value;
        }
    }

    // Stub class to simulate a node that throws an exception during calculation
    private static class ExceptionThrowingNode implements Node {
        @Override
        public Number calculate() {
            throw new ArithmeticException("Calculation error");
        }
    }

    // Interface to allow stubbing of the Node behavior
    private interface Node {
        Number calculate();
    }

    // Minimal stub for MathTree with controllable rootNode
    private static class TestableMathTree extends MathTree {
        private final Node rootNode;

        TestableMathTree(Node rootNode) {
            this.rootNode = rootNode;
        }

        @Override
        public Number solve() {
            if (rootNode == null)
                return null;
            else
                return rootNode.calculate();
        }
    }

    @Test
    void test_solve_returnsNull_whenRootNodeIsNull() {
        MathTree tree = new TestableMathTree(null);
        assertNull(tree.solve());
    }

    @Test
    void test_solve_returnsValue_whenRootNodeReturnsInteger() {
        MathTree tree = new TestableMathTree(new ValueNode(42));
        assertEquals(42, tree.solve());
    }

    @Test
    void test_solve_returnsValue_whenRootNodeReturnsDouble() {
        MathTree tree = new TestableMathTree(new ValueNode(3.14159));
        assertEquals(3.14159, (double) tree.solve(), 1e-9);
    }

    @Test
    void test_solve_returnsZero_whenRootNodeReturnsZero() {
        MathTree tree = new TestableMathTree(new ValueNode(0));
        assertEquals(0, tree.solve());
    }

    @Test
    void test_solve_returnsNegativeValue_whenRootNodeReturnsNegative() {
        MathTree tree = new TestableMathTree(new ValueNode(-100));
        assertEquals(-100, tree.solve());
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void test_solve_handlesSpecialFloatingPointValues(double specialValue) {
        MathTree tree = new TestableMathTree(new ValueNode(specialValue));
        assertEquals(specialValue, (double) tree.solve(), 1e-9);
    }

    @Test
    void test_solve_propagatesException_whenRootNodeThrows() {
        MathTree tree = new TestableMathTree(new ExceptionThrowingNode());
        assertThrows(ArithmeticException.class, tree::solve);
    }

    @Test
    void test_solve_returnsMaxInteger_whenRootNodeReturnsMaxInteger() {
        MathTree tree = new TestableMathTree(new ValueNode(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, tree.solve());
    }

    @Test
    void test_solve_returnsMinInteger_whenRootNodeReturnsMinInteger() {
        MathTree tree = new TestableMathTree(new ValueNode(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, tree.solve());
    }

    @Test
    void test_solve_returnsVeryLargeDouble_whenRootNodeReturnsVeryLargeDouble() {
        double veryLarge = 1e308;
        MathTree tree = new TestableMathTree(new ValueNode(veryLarge));
        assertEquals(veryLarge, (double) tree.solve(), 1e-9);
    }

    @Test
    void test_solve_returnsVerySmallPositiveDouble_whenRootNodeReturnsVerySmallDouble() {
        double verySmall = 1e-308;
        MathTree tree = new TestableMathTree(new ValueNode(verySmall));
        assertEquals(verySmall, (double) tree.solve(), 1e-9);
    }
}
