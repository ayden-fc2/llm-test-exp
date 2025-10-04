package mathNode;

import mathNode.Operator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OperatorCheckTreeGeneratedTest {

    // Stub implementation of Operator for testing purposes
    private static class TestOperator extends Operator {
        private final Operator left;
        private final Operator right;

        public TestOperator(Operator left, Operator right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean checkTree() {
            if (left == null || right == null)
                return false;
            else
                return left.checkTree() && right.checkTree();
        }

        @Override
        public Operator getLeftNode() {
            return left;
        }

        @Override
        public Operator getRightNode() {
            return right;
        }
    }

    // Leaf node that returns true (base case)
    private static class LeafOperator extends Operator {
        @Override
        public boolean checkTree() {
            return true;
        }

        @Override
        public Operator getLeftNode() {
            return null;
        }

        @Override
        public Operator getRightNode() {
            return null;
        }
    }

    @Test
    public void test_checkTree_bothChildrenNull_returnsFalse() {
        Operator op = new TestOperator(null, null);
        assertFalse(op.checkTree());
    }

    @Test
    public void test_checkTree_leftChildNull_returnsFalse() {
        Operator op = new TestOperator(null, new LeafOperator());
        assertFalse(op.checkTree());
    }

    @Test
    public void test_checkTree_rightChildNull_returnsFalse() {
        Operator op = new TestOperator(new LeafOperator(), null);
        assertFalse(op.checkTree());
    }

    @Test
    public void test_checkTree_bothChildrenLeafNodes_returnsTrue() {
        Operator op = new TestOperator(new LeafOperator(), new LeafOperator());
        assertTrue(op.checkTree());
    }

    @Test
    public void test_checkTree_leftSubtreeInvalid_returnsFalse() {
        Operator invalidLeft = new TestOperator(new LeafOperator(), null); // Right is null -> invalid
        Operator op = new TestOperator(invalidLeft, new LeafOperator());
        assertFalse(op.checkTree());
    }

    @Test
    public void test_checkTree_rightSubtreeInvalid_returnsFalse() {
        Operator invalidRight = new TestOperator(null, new LeafOperator()); // Left is null -> invalid
        Operator op = new TestOperator(new LeafOperator(), invalidRight);
        assertFalse(op.checkTree());
    }

    @Test
    public void test_checkTree_complexValidTree_returnsTrue() {
        Operator left = new TestOperator(new LeafOperator(), new LeafOperator());
        Operator right = new TestOperator(new LeafOperator(), new LeafOperator());
        Operator root = new TestOperator(left, right);
        assertTrue(root.checkTree());
    }

    @Test
    public void test_checkTree_complexInvalidTreeWithNull_returnsFalse() {
        Operator validLeft = new TestOperator(new LeafOperator(), new LeafOperator());
        Operator invalidRight = new TestOperator(new LeafOperator(), null);
        Operator root = new TestOperator(validLeft, invalidRight);
        assertFalse(root.checkTree());
    }
}
