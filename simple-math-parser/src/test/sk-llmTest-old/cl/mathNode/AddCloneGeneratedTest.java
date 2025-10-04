package mathNode;

import mathNode.Add;
import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class AddCloneGeneratedTest {

    private Add addNode;
    private Expression leftNode;
    private Expression rightNode;

    @BeforeEach
    public void setUp() {
        // Using mocks as minimal stubs for Expression
        leftNode = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
            
            @Override
            public double evaluate() {
                return 5.0;
            }
        };
        
        rightNode = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
            
            @Override
            public double evaluate() {
                return 3.0;
            }
        };
        
        addNode = new Add();
        addNode.setLeftNode(leftNode);
        addNode.setRightNode(rightNode);
    }

    @Test
    public void testClone_createsNewInstance() {
        Add cloned = (Add) addNode.clone();
        assertNotNull(cloned);
        assertNotSame(addNode, cloned);
    }

    @Test
    public void testClone_copiesLeftNode() {
        Add cloned = (Add) addNode.clone();
        assertNotNull(cloned.getLeftNode());
        assertNotSame(addNode.getLeftNode(), cloned.getLeftNode());
    }

    @Test
    public void testClone_copiesRightNode() {
        Add cloned = (Add) addNode.clone();
        assertNotNull(cloned.getRightNode());
        assertNotSame(addNode.getRightNode(), cloned.getRightNode());
    }

    @Test
    public void testClone_preservesValues() {
        Add cloned = (Add) addNode.clone();
        assertEquals(addNode.getLeftNode().evaluate(), cloned.getLeftNode().evaluate());
        assertEquals(addNode.getRightNode().evaluate(), cloned.getRightNode().evaluate());
    }
}
