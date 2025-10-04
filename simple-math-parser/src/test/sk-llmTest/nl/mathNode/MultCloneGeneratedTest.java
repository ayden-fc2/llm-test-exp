package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MultCloneGeneratedTest {

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

    static class Mult extends Expression {
        private Expression leftNode;
        private Expression rightNode;

        public Mult(Expression leftNode, Expression rightNode) {
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
            Mult clone = (Mult) super.clone();
            clone.setLeftNode((Expression) this.getLeftNode().clone());
            clone.setRightNode((Expression) this.getRightNode().clone());
            return clone;
        }
    }

    private Expression mockLeftNode;
    private Expression mockRightNode;
    private Mult multInstance;

    @BeforeEach
    void setUp() {
        mockLeftNode = new Expression(42);
        mockRightNode = new Expression("test");
        multInstance = new Mult(mockLeftNode, mockRightNode);
    }

    @Test
    void test_clone_normalCase_createsDeepCopy() throws CloneNotSupportedException {
        Mult cloned = (Mult) multInstance.clone();

        assertNotNull(cloned);
        assertNotSame(multInstance, cloned);
        assertEquals(multInstance.getLeftNode(), cloned.getLeftNode());
        assertEquals(multInstance.getRightNode(), cloned.getRightNode());
        assertNotSame(multInstance.getLeftNode(), cloned.getLeftNode());
        assertNotSame(multInstance.getRightNode(), cloned.getRightNode());
    }

    @Test
    void test_clone_withNullLeftNode_throwsNullPointerException() {
        Mult multWithNullLeft = new Mult(null, mockRightNode);

        assertThrows(NullPointerException.class, () -> multWithNullLeft.clone());
    }

    @Test
    void test_clone_withNullRightNode_throwsNullPointerException() {
        Mult multWithNullRight = new Mult(mockLeftNode, null);

        assertThrows(NullPointerException.class, () -> multWithNullRight.clone());
    }

    @Test
    void test_clone_withBothNodesNull_throwsNullPointerException() {
        Mult multWithBothNull = new Mult(null, null);

        assertThrows(NullPointerException.class, () -> multWithBothNull.clone());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE})
    void test_clone_withExtremeIntegerValues_preservesValues(int extremeValue) throws CloneNotSupportedException {
        Expression left = new Expression(extremeValue);
        Expression right = new Expression(extremeValue * 2);
        Mult multExtreme = new Mult(left, right);

        Mult cloned = (Mult) multExtreme.clone();

        assertNotNull(cloned);
        assertEquals(left, cloned.getLeftNode());
        assertEquals(right, cloned.getRightNode());
    }

    @Test
    void test_clone_withVeryLargeStrings_preservesValues() throws CloneNotSupportedException {
        String largeString = "a".repeat(10_000);
        Expression left = new Expression(largeString);
        Expression right = new Expression(new StringBuilder(largeString).reverse().toString());
        Mult multLargeString = new Mult(left, right);

        Mult cloned = (Mult) multLargeString.clone();

        assertNotNull(cloned);
        assertEquals(left, cloned.getLeftNode());
        assertEquals(right, cloned.getRightNode());
    }

    @Test
    void test_clone_isIdempotent_multipleClonesAreIndependent() throws CloneNotSupportedException {
        Mult firstClone = (Mult) multInstance.clone();
        Mult secondClone = (Mult) multInstance.clone();

        assertNotSame(firstClone, secondClone);
        assertEquals(firstClone.getLeftNode(), secondClone.getLeftNode());
        assertEquals(firstClone.getRightNode(), secondClone.getRightNode());
        assertNotSame(firstClone.getLeftNode(), secondClone.getLeftNode());
        assertNotSame(firstClone.getRightNode(), secondClone.getRightNode());
    }
}
