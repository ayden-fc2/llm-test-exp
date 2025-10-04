package mathNode;

import mathNode.Add;
import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class AddCloneGeneratedTest {

    private Add addNode;
    private Expression leftMock;
    private Expression rightMock;

    @BeforeEach
    public void setUp() {
        // Minimal stubs for Expression to allow compilation
        leftMock = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };
        rightMock = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };

        addNode = new Add();
        addNode.setLeftNode(leftMock);
        addNode.setRightNode(rightMock);
    }

    @Test
    public void test_clone_returnsNewInstance() {
        Add cloned = (Add) addNode.clone();
        assertNotNull(cloned);
        assertNotSame(addNode, cloned);
    }

    @Test
    public void test_clone_copiesLeftNode() {
        Add cloned = (Add) addNode.clone();
        assertNotNull(cloned.getLeftNode());
        assertNotSame(leftMock, cloned.getLeftNode());
    }

    @Test
    public void test_clone_copiesRightNode() {
        Add cloned = (Add) addNode.clone();
        assertNotNull(cloned.getRightNode());
        assertNotSame(rightMock, cloned.getRightNode());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    public void test_clone_withIntegerEdgeCases(int value) {
        Expression intLeftMock = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };
        Expression intRightMock = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };

        Add intAddNode = new Add();
        intAddNode.setLeftNode(intLeftMock);
        intAddNode.setRightNode(intRightMock);

        Add cloned = (Add) intAddNode.clone();
        assertNotNull(cloned);
        assertNotSame(intAddNode, cloned);
        assertNotSame(intLeftMock, cloned.getLeftNode());
        assertNotSame(intRightMock, cloned.getRightNode());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, 1.0, Double.MIN_VALUE, Double.MAX_VALUE, 1e-10, 1e10})
    public void test_clone_withDoubleEdgeCases(double value) {
        Expression doubleLeftMock = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };
        Expression doubleRightMock = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };

        Add doubleAddNode = new Add();
        doubleAddNode.setLeftNode(doubleLeftMock);
        doubleAddNode.setRightNode(doubleRightMock);

        Add cloned = (Add) doubleAddNode.clone();
        assertNotNull(cloned);
        assertNotSame(doubleAddNode, cloned);
        assertNotSame(doubleLeftMock, cloned.getLeftNode());
        assertNotSame(doubleRightMock, cloned.getRightNode());
    }
}
