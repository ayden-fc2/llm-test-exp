package mathNode;

import mathNode.Mult;
import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MultCloneGeneratedTest {

    // Stub implementation of Expression to enable compilation
    static abstract class Expression implements Cloneable {
        public abstract Object clone();
    }

    private Mult multInstance;
    private Expression leftNode;
    private Expression rightNode;

    @BeforeEach
    void setUp() {
        // Initialize with basic mock-like nodes that can be cloned
        leftNode = new Expression() {
            @Override
            public Object clone() {
                try {
                    return super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        rightNode = new Expression() {
            @Override
            public Object clone() {
                try {
                    return super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        multInstance = new Mult();
        multInstance.setLeftNode(leftNode);
        multInstance.setRightNode(rightNode);
    }

    @Test
    public void test_clone_createsNewInstance() {
        Mult clone = (Mult) multInstance.clone();
        assertNotNull(clone);
        assertNotSame(multInstance, clone); // Should be different objects
    }

    @Test
    public void test_clone_preservesLeftNodeValue() {
        Mult clone = (Mult) multInstance.clone();
        assertNotNull(clone.getLeftNode());
        assertNotSame(leftNode, clone.getLeftNode()); // Cloned node should also be a new instance
    }

    @Test
    public void test_clone_preservesRightNodeValue() {
        Mult clone = (Mult) multInstance.clone();
        assertNotNull(clone.getRightNode());
        assertNotSame(rightNode, clone.getRightNode()); // Cloned node should also be a new instance
    }

    @Test
    public void test_clone_withNullLeftNode() {
        multInstance.setLeftNode(null);
        Mult clone = (Mult) multInstance.clone();
        assertNull(clone.getLeftNode());
    }

    @Test
    public void test_clone_withNullRightNode() {
        multInstance.setRightNode(null);
        Mult clone = (Mult) multInstance.clone();
        assertNull(clone.getRightNode());
    }

    @Test
    public void test_clone_bothNodesNull() {
        multInstance.setLeftNode(null);
        multInstance.setRightNode(null);
        Mult clone = (Mult) multInstance.clone();
        assertNull(clone.getLeftNode());
        assertNull(clone.getRightNode());
    }
}
