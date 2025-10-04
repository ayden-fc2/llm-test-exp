package mathTree;

import mathTree.MathTree;
import mathTree.mathNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class MathTreeInsertNodeGeneratedTest {

    // Test when rootNode is null - newNode should become the root
    @Test
    public void testInsertNode_rootIsNull_returnsNewNode() {
        MathTree tree = new MathTree();
        mathNode newNode = new mathNode(5);
        
        mathNode result = tree.insertNode(null, newNode);
        
        assertSame(newNode, result, "When root is null, newNode should be returned");
    }

    // Test when newNode is null - should return existing root unchanged
    @Test
    public void testInsertNode_newNodeIsNull_returnsRootNode() {
        MathTree tree = new MathTree();
        mathNode rootNode = new mathNode(10);
        
        mathNode result = tree.insertNode(rootNode, null);
        
        assertSame(rootNode, result, "When newNode is null, root should be returned unchanged");
    }

    // Test with various integer values including edge cases
    @ParameterizedTest
    @ValueSource(ints = {0, 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    public void testInsertNode_withEdgeCaseValues_returnsNewNodeAsRootWhenRootIsNull(int value) {
        MathTree tree = new MathTree();
        mathNode newNode = new mathNode(value);
        
        mathNode result = tree.insertNode(null, newNode);
        
        assertSame(newNode, result, "New node should be returned regardless of value when root is null");
        assertEquals(value, result.value, "Node value should match input");
    }

    // Test inserting a node with a very large positive value
    @Test
    public void testInsertNode_veryLargePositiveValue_returnsNewNodeAsRoot() {
        MathTree tree = new MathTree();
        mathNode newNode = new mathNode(Integer.MAX_VALUE - 1);
        
        mathNode result = tree.insertNode(null, newNode);
        
        assertSame(newNode, result);
        assertEquals(Integer.MAX_VALUE - 1, result.value);
    }

    // Test inserting a node with a very small negative value
    @Test
    public void testInsertNode_verySmallNegativeValue_returnsNewNodeAsRoot() {
        MathTree tree = new MathTree();
        mathNode newNode = new mathNode(Integer.MIN_VALUE + 1);
        
        mathNode result = tree.insertNode(null, newNode);
        
        assertSame(newNode, result);
        assertEquals(Integer.MIN_VALUE + 1, result.value);
    }

    // Test behavior consistency with multiple sequential calls where newNode is null
    @Test
    public void testInsertNode_multipleCallsWithNullNewNode_alwaysReturnsRoot() {
        MathTree tree = new MathTree();
        mathNode rootNode = new mathNode(42);
        
        mathNode result1 = tree.insertNode(rootNode, null);
        mathNode result2 = tree.insertNode(rootNode, null);
        mathNode result3 = tree.insertNode(rootNode, null);
        
        assertSame(rootNode, result1);
        assertSame(rootNode, result2);
        assertSame(rootNode, result3);
    }

    // Test that both parameters can be the same object
    @Test
    public void testInsertNode_sameObjectForRootAndNewNode_returnsNewNodeWhenRootIsNull() {
        MathTree tree = new MathTree();
        mathNode node = new mathNode(7);
        
        mathNode result = tree.insertNode(null, node);
        
        assertSame(node, result);
    }

    // Test with zero value specifically
    @Test
    public void testInsertNode_zeroValue_returnsNewNodeAsRoot() {
        MathTree tree = new MathTree();
        mathNode newNode = new mathNode(0);
        
        mathNode result = tree.insertNode(null, newNode);
        
        assertSame(newNode, result);
        assertEquals(0, result.value);
    }
}
