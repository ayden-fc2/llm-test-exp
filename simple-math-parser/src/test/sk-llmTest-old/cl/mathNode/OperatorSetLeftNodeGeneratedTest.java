package mathNode;

import mathNode.Operator;
import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class OperatorSetLeftNodeGeneratedTest {

    private Operator operator;
    private Expression mockExpression;

    @BeforeEach
    public void setUp() {
        // Create a minimal stub for Expression since it's a collaborator
        mockExpression = new Expression() {
            @Override
            public double evaluate() { return 0.0; }
            
            @Override
            public String toString() { return "MockExpression"; }
        };
        
        // Assuming Operator has a default constructor
        operator = new Operator();
    }

    @Test
    public void test_setLeftNode_withValidNode() {
        // Arrange
        Expression newNode = mockExpression;
        
        // Act
        operator.setLeftNode(newNode);
        
        // Assert
        // Using reflection or assuming there's a getter to verify state
        // Since we don't have access to leftNode directly, we can only test that no exception is thrown
        assertDoesNotThrow(() -> operator.setLeftNode(newNode));
    }

    @Test
    public void test_setLeftNode_withNullNode() {
        // Arrange
        Expression newNode = null;
        
        // Act
        operator.setLeftNode(newNode);
        
        // Assert
        assertDoesNotThrow(() -> operator.setLeftNode(newNode));
    }

    @Test
    public void test_setLeftNode_multipleTimes() {
        // Arrange
        Expression firstNode = mockExpression;
        Expression secondNode = new Expression() {
            @Override
            public double evaluate() { return 1.0; }
            
            @Override
            public String toString() { return "SecondMockExpression"; }
        };
        
        // Act & Assert
        assertDoesNotThrow(() -> operator.setLeftNode(firstNode));
        assertDoesNotThrow(() -> operator.setLeftNode(secondNode));
        assertDoesNotThrow(() -> operator.setLeftNode(null));
    }
}
