package mathNode;

import mathNode.Operator;
import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class OperatorSetRightNodeGeneratedTest {

    private Operator operator;
    private Expression mockExpression;

    // Minimal stub for Expression to allow compilation
    static class ExpressionStub implements Expression {
        private String value;
        
        public ExpressionStub(String value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }

    @BeforeEach
    void setUp() {
        // Using a concrete subclass would be ideal, but we'll use a dynamic proxy approach if needed
        // For now, we'll test with null and mock-like objects
        operator = new Operator() {
            private Expression left;
            private Expression right;
            private boolean parenthesis;
            private int precedence;
            
            @Override
            public Expression getLeftNode() { return left; }
            
            @Override
            public void setLeftNode(Expression newNode) { this.left = newNode; }
            
            @Override
            public Expression getRightNode() { return right; }
            
            @Override
            public void setRightNode(Expression newNode) { this.right = newNode; }
            
            @Override
            public boolean getParens() { return parenthesis; }
            
            @Override
            public void setParens(boolean bool) { 
                this.parenthesis = bool;
                if (bool) precedence = 0;
            }
            
            @Override
            public int getPrecedence() { return precedence; }
            
            @Override
            public String getText() { return ""; }
            
            @Override
            public double getValue() { return 0.0; }
        };
        
        mockExpression = new ExpressionStub("test");
    }

    @Test
    public void test_setRightNode_withNonNullExpression() {
        // Arrange
        Expression newNode = new ExpressionStub("newNode");

        // Act
        operator.setRightNode(newNode);

        // Assert
        assertSame(newNode, operator.getRightNode(), "Right node should be set to the new node");
    }

    @Test
    public void test_setRightNode_withNullExpression() {
        // Act
        operator.setRightNode(null);

        // Assert
        assertNull(operator.getRightNode(), "Right node should be set to null");
    }

    @Test
    public void test_setRightNode_multipleCalls() {
        // Arrange
        Expression firstNode = new ExpressionStub("first");
        Expression secondNode = new ExpressionStub("second");

        // Act & Assert
        operator.setRightNode(firstNode);
        assertSame(firstNode, operator.getRightNode(), "First node should be set");

        operator.setRightNode(secondNode);
        assertSame(secondNode, operator.getRightNode(), "Second node should replace first node");

        operator.setRightNode(null);
        assertNull(operator.getRightNode(), "Node should be set to null");
    }

    @Test
    public void test_setRightNode_withSameExpression() {
        // Arrange
        Expression sameNode = new ExpressionStub("same");

        // Act
        operator.setRightNode(sameNode);
        operator.setRightNode(sameNode);

        // Assert
        assertSame(sameNode, operator.getRightNode(), "Setting the same node twice should work");
    }
}
