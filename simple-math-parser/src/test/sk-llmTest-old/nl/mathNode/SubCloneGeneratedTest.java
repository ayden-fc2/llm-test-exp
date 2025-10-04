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
            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    static class Sub extends Expression implements Cloneable {
        private Expression leftNode;
        private Expression rightNode;

        public Sub(Expression leftNode, Expression rightNode) {
            super(null);
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

    private Expression left;
    private Expression right;
    private Sub sub;

    @BeforeEach
    void setUp() {
        left = new Expression(5);
        right = new Expression(3);
        sub = new Sub(left, right);
    }

    @Test
    void test_clone_createsNewInstance() throws CloneNotSupportedException {
        Sub cloned = (Sub) sub.clone();
        assertNotNull(cloned);
        assertNotSame(sub, cloned);
    }

    @Test
    void test_clone_preservesLeftNodeValue() throws CloneNotSupportedException {
        Sub cloned = (Sub) sub.clone();
        assertEquals(sub.getLeftNode(), cloned.getLeftNode());
        assertNotSame(sub.getLeftNode(), cloned.getLeftNode());
    }

    @Test
    void test_clone_preservesRightNodeValue() throws CloneNotSupportedException {
        Sub cloned = (Sub) sub.clone();
        assertEquals(sub.getRightNode(), cloned.getRightNode());
        assertNotSame(sub.getRightNode(), cloned.getRightNode());
    }

    @Test
    void test_clone_withNullLeftNode() throws CloneNotSupportedException {
        sub.setLeftNode(null);
        assertThrows(NullPointerException.class, () -> sub.clone());
    }

    @Test
    void test_clone_withNullRightNode() throws CloneNotSupportedException {
        sub.setRightNode(null);
        assertThrows(NullPointerException.class, () -> sub.clone());
    }

    @Test
    void test_clone_withBothNodesNull() throws CloneNotSupportedException {
        sub.setLeftNode(null);
        sub.setRightNode(null);
        assertThrows(NullPointerException.class, () -> sub.clone());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE})
    void test_clone_withExtremeIntegerValues(int value) throws CloneNotSupportedException {
        Expression leftExtreme = new Expression(value);
        Expression rightExtreme = new Expression(-value);
        Sub extremeSub = new Sub(leftExtreme, rightExtreme);

        Sub cloned = (Sub) extremeSub.clone();

        assertEquals(extremeSub.getLeftNode(), cloned.getLeftNode());
        assertEquals(extremeSub.getRightNode(), cloned.getRightNode());
        assertNotSame(extremeSub, cloned);
        assertNotSame(extremeSub.getLeftNode(), cloned.getLeftNode());
        assertNotSame(extremeSub.getRightNode(), cloned.getRightNode());
    }

    @Test
    void test_clone_withFloatingPointValues() throws CloneNotSupportedException {
        Expression leftFloat = new Expression(1e-9);
        Expression rightFloat = new Expression(1e+9);
        Sub floatSub = new Sub(leftFloat, rightFloat);

        Sub cloned = (Sub) floatSub.clone();

        assertEquals(floatSub.getLeftNode(), cloned.getLeftNode());
        assertEquals(floatSub.getRightNode(), cloned.getRightNode());
        assertNotSame(floatSub, cloned);
        assertNotSame(floatSub.getLeftNode(), cloned.getLeftNode());
        assertNotSame(floatSub.getRightNode(), cloned.getRightNode());
    }

    @Test
    void test_clone_isIdempotent() throws CloneNotSupportedException {
        Sub firstClone = (Sub) sub.clone();
        Sub secondClone = (Sub) firstClone.clone();

        assertEquals(firstClone.getLeftNode(), secondClone.getLeftNode());
        assertEquals(firstClone.getRightNode(), secondClone.getRightNode());
        assertNotSame(firstClone, secondClone);
        assertNotSame(firstClone.getLeftNode(), secondClone.getLeftNode());
        assertNotSame(firstClone.getRightNode(), secondClone.getRightNode());
    }
}
