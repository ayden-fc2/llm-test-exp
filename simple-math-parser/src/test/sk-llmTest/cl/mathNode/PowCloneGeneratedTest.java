package mathNode;

import mathNode.Pow;
import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class PowCloneGeneratedTest {

    private Pow powNode;
    private Expression leftNode;
    private Expression rightNode;

    @BeforeEach
    void setUp() {
        // Using minimal stubs for Expression since it's not provided
        leftNode = new Expression() {
            @Override
            public Object clone() {
                return new Expression() {
                    @Override
                    public Object clone() {
                        return this;
                    }
                };
            }
        };
        rightNode = new Expression() {
            @Override
            public Object clone() {
                return new Expression() {
                    @Override
                    public Object clone() {
                        return this;
                    }
                };
            }
        };
        powNode = new Pow();
        powNode.setLeftNode(leftNode);
        powNode.setRightNode(rightNode);
    }

    @Test
    void test_clone_createsNewInstance() {
        Pow cloned = (Pow) powNode.clone();
        assertNotNull(cloned);
        assertNotSame(powNode, cloned);
    }

    @Test
    void test_clone_copiesLeftNode() {
        Pow cloned = (Pow) powNode.clone();
        assertNotNull(cloned.getLeftNode());
        assertNotSame(powNode.getLeftNode(), cloned.getLeftNode());
    }

    @Test
    void test_clone_copiesRightNode() {
        Pow cloned = (Pow) powNode.clone();
        assertNotNull(cloned.getRightNode());
        assertNotSame(powNode.getRightNode(), cloned.getRightNode());
    }

    @Test
    void test_clone_preservesNodeType() {
        Pow cloned = (Pow) powNode.clone();
        assertTrue(cloned instanceof Pow);
    }

    @Test
    void test_clone_withNullLeftNode() {
        powNode.setLeftNode(null);
        Pow cloned = (Pow) powNode.clone();
        assertNull(cloned.getLeftNode());
    }

    @Test
    void test_clone_withNullRightNode() {
        powNode.setRightNode(null);
        Pow cloned = (Pow) powNode.clone();
        assertNull(cloned.getRightNode());
    }
}
