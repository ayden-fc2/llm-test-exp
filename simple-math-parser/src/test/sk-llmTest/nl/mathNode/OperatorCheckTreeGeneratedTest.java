package mathNode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OperatorCheckTreeGeneratedTest {

    // 最小桩类，用于构造测试场景
    static class NodeStub {
        NodeStub left;
        NodeStub right;

        NodeStub(NodeStub left, NodeStub right) {
            this.left = left;
            this.right = right;
        }

        boolean checkTree() {
            if (left == null || right == null)
                return false;
            else
                return left.checkTree() && right.checkTree();
        }
    }

    @Test
    void test_checkTree_leftNodeIsNull_returnsFalse() {
        NodeStub root = new NodeStub(null, new NodeStub(null, null));
        assertFalse(root.checkTree());
    }

    @Test
    void test_checkTree_rightNodeIsNull_returnsFalse() {
        NodeStub root = new NodeStub(new NodeStub(null, null), null);
        assertFalse(root.checkTree());
    }

    @Test
    void test_checkTree_bothChildrenInvalid_returnsFalse() {
        NodeStub root = new NodeStub(new NodeStub(null, null), new NodeStub(new NodeStub(null, null), null));
        assertFalse(root.checkTree());
    }

    @Test
    void test_checkTree_allNodesValid_returnsTrue() {
        NodeStub leaf1 = new NodeStub(null, null);
        NodeStub leaf2 = new NodeStub(null, null);
        NodeStub leaf3 = new NodeStub(null, null);
        NodeStub leaf4 = new NodeStub(null, null);
        NodeStub left = new NodeStub(leaf1, leaf2);
        NodeStub right = new NodeStub(leaf3, leaf4);
        NodeStub root = new NodeStub(left, right);
        assertTrue(root.checkTree());
    }

    @Test
    void test_checkTree_singleLevelInvalid_returnsFalse() {
        NodeStub root = new NodeStub(new NodeStub(null, null), new NodeStub(null, null));
        // This is a valid tree
        assertTrue(root.checkTree());
    }

    @Test
    void test_checkTree_deepInvalidBranch_returnsFalse() {
        NodeStub badLeaf = new NodeStub(null, new NodeStub(null, null)); // only one child
        NodeStub goodLeft = new NodeStub(new NodeStub(null, null), new NodeStub(null, null));
        NodeStub badRight = new NodeStub(badLeaf, new NodeStub(null, null));
        NodeStub root = new NodeStub(goodLeft, badRight);
        assertFalse(root.checkTree());
    }

    @Test
    void test_checkTree_emptyTree_returnsFalse() {
        NodeStub root = new NodeStub(null, null);
        assertFalse(root.checkTree());
    }
}
