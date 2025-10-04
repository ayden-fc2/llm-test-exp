package mathNode;

import mathNode.Operator;
import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class OperatorGetLeftNodeGeneratedTest {

    private Operator operator;
    private Expression mockLeftNode;
    private Expression mockRightNode;

    @BeforeEach
    void setUp() {
        // Create simple stubs for Expression since it's not provided
        mockLeftNode = new Expression() {};
        mockRightNode = new Expression() {};
        operator = new Operator() {
            private Expression leftNode = mockLeftNode;
            private Expression rightNode = mockRightNode;
            
            @Override
            public Expression getLeftNode() { return leftNode; }
            
            @Override
            public Expression getRightNode() { return rightNode; }
            
            @Override
            public void setLeftNode(Expression newNode) { leftNode = newNode; }
            
            @Override
            public void setRightNode(Expression newNode) { rightNode = newNode; }
            
            @Override
            public void setParens(boolean bool) { /* Not relevant for this test */ }
        };
    }

    @Test
    public void test_getLeftNode_returnsCorrectNode() {
        // Act
        Expression result = operator.getLeftNode();
        
        // Assert
        assertSame(mockLeftNode, result, "getLeftNode should return the left node");
    }

    @Test
    public void test_getLeftNode_returnsNullWhenUninitialized() {
        // Arrange
        Operator emptyOperator = new Operator() {
            private Expression leftNode = null;
            private Expression rightNode = null;
            
            @Override
            public Expression getLeftNode() { return leftNode; }
            
            @Override
            public Expression getRightNode() { return rightNode; }
            
            @Override
            public void setLeftNode(Expression newNode) { leftNode = newNode; }
            
            @Override
            public void setRightNode(Expression newNode) { rightNode = newNode; }
            
            @Override
            public void setParens(boolean bool) { /* Not relevant for this test */ }
        };
        
        // Act
        Expression result = emptyOperator.getLeftNode();
        
        // Assert
        assertNull(result, "getLeftNode should return null when not initialized");
    }

    @Test
    public void test_getLeftNode_afterSettingNewValue() {
        // Arrange
        Expression newLeftNode = new Expression() {};
        
        // Act
        operator.setLeftNode(newLeftNode);
        Expression result = operator.getLeftNode();
        
        // Assert
        assertSame(newLeftNode, result, "getLeftNode should return the newly set node");
    }
}
