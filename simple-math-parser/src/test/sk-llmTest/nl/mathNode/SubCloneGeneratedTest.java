package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SubCloneGeneratedTest {

    // Minimal stubs to support compilation and testing
    static class Expression implements Cloneable {
        private final Object value;

        public Expression(Object value) {
            this.value = value;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Expression)) return false;
            Expression that = (Expression) o;
            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    static class Sub extends Expression implements Cloneable {
        private Expression leftNode;
        private Expression rightNode;

        public Sub(Expression leftNode, Expression rightNode) {
            super("Sub");
            this.leftNode = leftNode;
            this.rightNode = rightNode;
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
            Sub clone = (Sub) super.clone();
            clone.setLeftNode((Expression) this.getLeftNode().clone());
            clone.setRightNode((Expression) this.getRightNode().clone());
            return clone;
        }
    }

    private Expression leftExpr;
    private Expression rightExpr;
    private Sub sub;

    @BeforeEach
    void setUp() {
        leftExpr = new Expression(5);
        rightExpr = new Expression(3);
        sub = new Sub(leftExpr, rightExpr);
    }

    @Test
    void test_clone_createsNewInstance() throws CloneNotSupportedException {
        Sub cloned = (Sub) sub.clone();
        assertNotNull(cloned);
        assertNotSame(sub, cloned);
    }

    @Test
    void test_clone_preservesValues() throws CloneNotSupportedException {
        Sub cloned = (Sub) sub.clone();
        assertEquals(sub.getLeftNode(), cloned.getLeftNode());
        assertEquals(sub.getRightNode(), cloned.getRightNode());
    }

    @Test
    void test_clone_leftNodeIsClonedIndependently() throws CloneNotSupportedException {
        Sub cloned = (Sub) sub.clone();
        assertNotSame(sub.getLeftNode(), cloned.getLeftNode());
        assertEquals(sub.getLeftNode(), cloned.getLeftNode());
    }

    @Test
    void test_clone_rightNodeIsClonedIndependently() throws CloneNotSupportedException {
        Sub cloned = (Sub) sub.clone();
        assertNotSame(sub.getRightNode(), cloned.getRightNode());
        assertEquals(sub.getRightNode(), cloned.getRightNode());
    }

    @Test
    void test_clone_withNullLeftNode_throwsNullPointerException() {
        Sub subWithNullLeft = new Sub(null, rightExpr);
        assertThrows(NullPointerException.class, () -> subWithNullLeft.clone());
    }

    @Test
    void test_clone_withNullRightNode_throwsNullPointerException() {
        Sub subWithNullRight = new Sub(leftExpr, null);
        assertThrows(NullPointerException.class, () -> subWithNullRight.clone());
    }

    @Test
    void test_clone_withBothNodesNull_throwsNullPointerException() {
        Sub subWithBothNull = new Sub(null, null);
        assertThrows(NullPointerException.class, () -> subWithBothNull.clone());
    }

    @Test
    void test_clone_whenLeftNodeCloneThrows_exceptionPropagates() {
        Expression leftThatThrows = new Expression("throws") {
            @Override
            public Object clone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("Simulated failure");
            }
        };
        Sub subWithFaultyLeft = new Sub(leftThatThrows, rightExpr);
        assertThrows(CloneNotSupportedException.class, () -> subWithFaultyLeft.clone());
    }

    @Test
    void test_clone_whenRightNodeCloneThrows_exceptionPropagates() {
        Expression rightThatThrows = new Expression("throws") {
            @Override
            public Object clone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("Simulated failure");
            }
        };
        Sub subWithFaultyRight = new Sub(leftExpr, rightThatThrows);
        assertThrows(CloneNotSupportedException.class, () -> subWithFaultyRight.clone());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void test_clone_withBoundaryValueExpressions(int val) throws CloneNotSupportedException {
        Expression left = new Expression(val);
        Expression right = new Expression(val + 1);
        Sub subWithBoundaries = new Sub(left, right);

        Sub cloned = (Sub) subWithBoundaries.clone();
        assertEquals(left, cloned.getLeftNode());
        assertEquals(right, cloned.getRightNode());
    }

    @Test
    void test_clone_isNotShallowCopy() throws CloneNotSupportedException {
        Sub cloned = (Sub) sub.clone();
        cloned.getLeftNode().equals(new Expression(999)); // Modifying clone should not affect original
        assertEquals(new Expression(5), sub.getLeftNode());
    }

    @Test
    void test_clone_returnsCorrectType() throws CloneNotSupportedException {
        Object cloned = sub.clone();
        assertTrue(cloned instanceof Sub);
    }
}
