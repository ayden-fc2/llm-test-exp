package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// 最小桩类，用于模拟 Expression 和 Mult 的依赖关系
abstract class MultCloneGeneratedTest implements Cloneable {
    public abstract Object clone() throws CloneNotSupportedException;
}

class Mult implements Cloneable {
    private Expression leftNode;
    private Expression rightNode;

    public Mult(Expression left, Expression right) {
        this.leftNode = left;
        this.rightNode = right;
    }

    public Expression getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Expression leftNode) {
        this.leftNode = leftNode;
    }

    public Expression getRightNode() {
        return rightNode;
    }

    public void setRightNode(Expression rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Mult clone = (Mult) super.clone();
        clone.setLeftNode((Expression) this.getLeftNode().clone());
        clone.setRightNode((Expression) this.getRightNode().clone());
        return clone;
    }
}

// 测试类
public class MultCloneTest {

    private Expression mockLeft;
    private Expression mockRight;

    @BeforeEach
    void setUp() {
        mockLeft = new Expression() {
            @Override
            public Object clone() {
                return new Expression() {}; // 简单克隆返回新实例
            }
        };

        mockRight = new Expression() {
            @Override
            public Object clone() {
                return new Expression() {}; // 简单克隆返回新实例
            }
        };
    }

    @Test
    public void test_clone_normalCase_createsDeepCopy() throws CloneNotSupportedException {
        Mult original = new Mult(mockLeft, mockRight);
        Mult cloned = (Mult) original.clone();

        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertNotSame(original.getLeftNode(), cloned.getLeftNode());
        assertNotSame(original.getRightNode(), cloned.getRightNode());
        assertEquals(original.getLeftNode().getClass(), cloned.getLeftNode().getClass());
        assertEquals(original.getRightNode().getClass(), cloned.getRightNode().getClass());
    }

    @Test
    public void test_clone_withNullLeftNode_throwsNullPointerException() {
        Mult original = new Mult(null, mockRight);

        assertThrows(NullPointerException.class, () -> original.clone());
    }

    @Test
    public void test_clone_withNullRightNode_throwsNullPointerException() {
        Mult original = new Mult(mockLeft, null);

        assertThrows(NullPointerException.class, () -> original.clone());
    }

    @Test
    public void test_clone_withBothNodesNull_throwsNullPointerException() {
        Mult original = new Mult(null, null);

        assertThrows(NullPointerException.class, () -> original.clone());
    }

    @Test
    public void test_clone_whenLeftCloneThrows_exceptionPropagates() {
        Expression leftThatThrows = new Expression() {
            @Override
            public Object clone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("Left node not cloneable");
            }
        };

        Mult original = new Mult(leftThatThrows, mockRight);

        assertThrows(CloneNotSupportedException.class, () -> original.clone());
    }

    @Test
    public void test_clone_whenRightCloneThrows_exceptionPropagates() {
        Expression rightThatThrows = new Expression() {
            @Override
            public Object clone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("Right node not cloneable");
            }
        };

        Mult original = new Mult(mockLeft, rightThatThrows);

        assertThrows(CloneNotSupportedException.class, () -> original.clone());
    }

    @Test
    public void test_clone_returnsCorrectType() throws CloneNotSupportedException {
        Mult original = new Mult(mockLeft, mockRight);
        Object result = original.clone();

        assertTrue(result instanceof Mult);
    }
}
