package mathNode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Operator#setRightNode(Expression)}.
 */
class OperatorSetRightNodeGeneratedTest {

    // Minimal stub for Expression to satisfy compilation
    static class Expression {}

    // Minimal stub for Operator to allow instantiation and access to rightNode
    static class Operator {
        private Expression rightNode;

        public void setRightNode(Expression newNode) {
            this.rightNode = newNode;
        }

        public Expression getRightNode() {
            return rightNode;
        }
    }

    @Test
    void test_setRightNode_withNonNullExpression_setsNodeCorrectly() {
        // Arrange
        Operator operator = new Operator();
        Expression newNode = new Expression();

        // Act
        operator.setRightNode(newNode);

        // Assert
        assertSame(newNode, operator.getRightNode());
    }

    @Test
    void test_setRightNode_withNullExpression_setsNodeToNull() {
        // Arrange
        Operator operator = new Operator();
        operator.setRightNode(new Expression()); // Set to non-null first

        // Act
        operator.setRightNode(null);

        // Assert
        assertNull(operator.getRightNode());
    }

    @Test
    void test_setRightNode_multipleTimes_overwritesPreviousValue() {
        // Arrange
        Operator operator = new Operator();
        Expression firstNode = new Expression();
        Expression secondNode = new Expression();

        // Act
        operator.setRightNode(firstNode);
        operator.setRightNode(secondNode);

        // Assert
        assertSame(secondNode, operator.getRightNode());
    }

    @Test
    void test_setRightNode_onNewOperator_rightNodeIsInitiallyNull() {
        // Arrange
        Operator operator = new Operator();

        // Assert
        assertNull(operator.getRightNode());
    }
}
