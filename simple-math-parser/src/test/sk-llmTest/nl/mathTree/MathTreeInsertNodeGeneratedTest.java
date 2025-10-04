package mathTree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MathTreeInsertNodeGeneratedTest {

    // Minimal stubs for mathNode and its subclasses to enable compilation
    static abstract class mathNode {
        private mathNode left;
        private mathNode right;

        public mathNode getLeftNode() { return left; }
        public mathNode getRightNode() { return right; }
        public void setLeftNode(mathNode node) { this.left = node; }
        public void setRightNode(mathNode node) { this.right = node; }

        static class Operator extends mathNode {
            private final int precedence;
            private final boolean parens;

            public Operator(int precedence, boolean parens) {
                this.precedence = precedence;
                this.parens = parens;
            }

            public int getPrecedence() { return precedence; }
            public boolean isParens() { return parens; }

            @Override
            public String toString() { return "Operator"; }
        }

        static class Int extends mathNode {
            private final int value;

            public Int(int value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "Int:" + value;
            }
        }

        static class Dec extends mathNode {
            private final double value;

            public Dec(double value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "Dec:" + value;
            }
        }
    }

    private final MathTree mathTree = new MathTree();

    // Test: Inserting newNode into null root returns newNode
    @Test
    void test_InsertNode_NullRoot_ReturnsNewNode() {
        mathNode newNode = new mathNode.Int(42);
        mathNode result = mathTree.insertNode(null, newNode);
        assertSame(newNode, result);
    }

    // Test: Inserting null newNode into existing root returns root unchanged
    @Test
    void test_InsertNode_NullNewNode_ReturnsRootUnchanged() {
        mathNode rootNode = new mathNode.Int(10);
        mathNode result = mathTree.insertNode(rootNode, null);
        assertSame(rootNode, result);
    }

    // Test: Inserting non-operator newNode when root is not an operator -> invalid case
    @Test
    void test_InsertNode_NonOperatorNewNode_RootNotOperator_ReturnsNull() {
        mathNode rootNode = new mathNode.Int(5);
        mathNode newNode = new mathNode.Int(10);
        mathNode result = mathTree.insertNode(rootNode, newNode);
        assertNull(result);
    }

    // Test: Inserting operator newNode with higher precedence than root operator
    @Test
    void test_InsertNode_OperatorHigherPrecedence_UpdatesRoot() {
        mathNode.Operator rootOp = new mathNode.Operator(1, false); // lower precedence
        mathNode.Operator newOp = new mathNode.Operator(2, false);  // higher precedence
        mathNode operand = new mathNode.Int(3);

        rootOp.setRightNode(operand);

        mathNode result = mathTree.insertNode(rootOp, newOp);

        assertSame(newOp, result);
        assertSame(rootOp, newOp.getLeftNode());
        assertSame(operand, newOp.getRightNode());
    }

    // Test: Inserting operator newNode with equal precedence to root operator
    @Test
    void test_InsertNode_OperatorEqualPrecedence_UpdatesRoot() {
        mathNode.Operator rootOp = new mathNode.Operator(2, false);
        mathNode.Operator newOp = new mathNode.Operator(2, false);
        mathNode operand = new mathNode.Int(7);

        rootOp.setRightNode(operand);

        mathNode result = mathTree.insertNode(rootOp, newOp);

        assertSame(newOp, result);
        assertSame(rootOp, newOp.getLeftNode());
        assertSame(operand, newOp.getRightNode());
    }

    // Test: Inserting operator newNode with lower precedence than root operator
    @Test
    void test_InsertNode_OperatorLowerPrecedence_AttachesToRightSubtree() {
        mathNode.Operator rootOp = new mathNode.Operator(3, false); // higher precedence
        mathNode.Operator newOp = new mathNode.Operator(1, false);  // lower precedence
        mathNode operand1 = new mathNode.Int(4);
        mathNode operand2 = new mathNode.Int(5);

        rootOp.setRightNode(operand1);

        mathNode result = mathTree.insertNode(rootOp, newOp);

        assertSame(rootOp, result);
        assertSame(newOp, rootOp.getRightNode());
        assertSame(operand1, newOp.getLeftNode());
        assertSame(operand2, newOp.getRightNode()); // Not attached yet, but structure should be valid
    }

    // Test: Inserting operator newNode where parent's right child is null -> invalid
    @Test
    void test_InsertNode_MissingValueBetweenOperators_ReturnsNull() {
        mathNode.Operator rootOp = new mathNode.Operator(3, false);
        mathNode.Operator newOp = new mathNode.Operator(1, false);

        mathNode result = mathTree.insertNode(rootOp, newOp);

        assertNull(result);
    }

    // Test: Inserting parenthesized operator (should go as normal number-like node)
    @Test
    void test_InsertNode_ParenthesizedOperator_GoesToRightmost() {
        mathNode.Operator rootOp = new mathNode.Operator(1, false);
        mathNode.Operator parenOp = new mathNode.Operator(2, true); // parenthesized
        mathNode operand1 = new mathNode.Int(6);
        mathNode operand2 = new mathNode.Dec(2.5);

        rootOp.setLeftNode(operand1);
        rootOp.setRightNode(operand2);

        mathNode result = mathTree.insertNode(rootOp, parenOp);

        assertSame(rootOp, result);
        assertSame(parenOp, operand2.getRightNode());
    }

    // Test: Inserting integer node to rightmost position
    @Test
    void test_InsertNode_IntegerNode_AttachesToRightmost() {
        mathNode.Operator rootOp = new mathNode.Operator(1, false);
        mathNode operand1 = new mathNode.Int(8);
        mathNode operand2 = new mathNode.Int(9);
        mathNode newNode = new mathNode.Int(10);

        rootOp.setLeftNode(operand1);
        rootOp.setRightNode(operand2);

        mathNode result = mathTree.insertNode(rootOp, newNode);

        assertSame(rootOp, result);
        assertSame(newNode, operand2.getRightNode());
    }

    // Test: Inserting decimal node to rightmost position
    @Test
    void test_InsertNode_DecimalNode_AttachesToRightmost() {
        mathNode.Operator rootOp = new mathNode.Operator(2, false);
        mathNode operand1 = new mathNode.Dec(3.14);
        mathNode operand2 = new mathNode.Int(15);
        mathNode newNode = new mathNode.Dec(9.99);

        rootOp.setLeftNode(operand1);
        rootOp.setRightNode(operand2);

        mathNode result = mathTree.insertNode(rootOp, newNode);

        assertSame(rootOp, result);
        assertSame(newNode, operand2.getRightNode());
    }

    // Test: Invalid case - two numbers in a row without operator
    @Test
    void test_InsertNode_TwoNumbersInARow_ReturnsNull() {
        mathNode.Operator rootOp = new mathNode.Operator(1, false);
        mathNode operand1 = new mathNode.Int(1);
        mathNode operand2 = new mathNode.Dec(2.0);
        mathNode newNode = new mathNode.Int(3);

        rootOp.setLeftNode(operand1);
        rootOp.setRightNode(operand2);

        // Manually simulate full right branch being filled
        operand2.setRightNode(newNode); // This makes it invalid

        mathNode result = mathTree.insertNode(rootOp, new mathNode.Int(4));

        assertNull(result);
    }

    // Parameterized test for various numeric values including edge cases
    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void test_InsertNode_IntegerEdgeCases(int val) {
        mathNode.Operator rootOp = new mathNode.Operator(1, false);
        mathNode operand = new mathNode.Int(100);
        mathNode newNode = new mathNode.Int(val);

        rootOp.setLeftNode(operand);

        mathNode result = mathTree.insertNode(rootOp, newNode);

        assertSame(rootOp, result);
        assertSame(newNode, rootOp.getRightNode());
    }

    // Parameterized test for decimal edge cases
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.0, Double.MAX_VALUE, Double.MIN_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void test_InsertNode_DoubleEdgeCases(double val) {
        mathNode.Operator rootOp = new mathNode.Operator(2, false);
        mathNode operand = new mathNode.Int(200);
        mathNode newNode = new mathNode.Dec(val);

        rootOp.setLeftNode(operand);

        mathNode result = mathTree.insertNode(rootOp, newNode);

        assertSame(rootOp, result);
        assertSame(newNode, rootOp.getRightNode());
    }
}
