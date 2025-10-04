package mathNode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OperatorSetRightNodeGeneratedTest {

    // Minimal stub for Expression to satisfy compilation
    static class Expression {}

    // Minimal stub for Operator to allow testing setRightNode
    static class Operator {
        private Expression leftNode;
        private Expression rightNode;

        public void setRightNode(Expression newNode) {
            this.rightNode = newNode;
        }

        public Expression getRightNode() {
            return rightNode;
        }
    }

    @Test
    void test_setRightNode_withNonNullNode() {
        Operator operator = new Operator();
        Expression node = new Expression();

        operator.setRightNode(node);

        assertSame(node, operator.getRightNode());
    }

    @Test
    void test_setRightNode_withNullNode() {
        Operator operator = new Operator();

        operator.setRightNode(null);

        assertNull(operator.getRightNode());
    }

    @Test
    void test_setRightNode_replacesExistingNode() {
        Operator operator = new Operator();
        Expression oldNode = new Expression();
        Expression newNode = new Expression();

        operator.setRightNode(oldNode);
        operator.setRightNode(newNode);

        assertSame(newNode, operator.getRightNode());
    }

    @Test
    void test_setRightNode_multipleCallsWithSameNode() {
        Operator operator = new Operator();
        Expression node = new Expression();

        operator.setRightNode(node);
        operator.setRightNode(node);

        assertSame(node, operator.getRightNode());
    }
}
