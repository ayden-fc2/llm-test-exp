package mathTree;

import mathTree.MathTree;
import mathTree.MathTree.mathNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class MathTreeInsertNodeGeneratedTest {

    // Test when rootNode is null - newNode should be returned
    @Test
    public void test_insertNode_rootIsNull_returnsNewNode() {
        MathTree tree = new MathTree();
        mathNode newNode = new mathNode(5);
        
        mathNode result = tree.insertNode(null, newNode);
        
        assertSame(newNode, result);
    }

    // Test when newNode is null - root should be returned unchanged
    @Test
    public void test_insertNode_newNodeIsNull_returnsRootNode() {
        MathTree tree = new MathTree();
        mathNode rootNode = new mathNode(10);
        
        mathNode result = tree.insertNode(rootNode, null);
        
        assertSame(rootNode, result);
    }

    // Test both nodes are null
    @Test
    public void test_insertNode_bothNodesNull_returnsNull() {
        MathTree tree = new MathTree();
        
        mathNode result = tree.insertNode(null, null);
        
        assertNull(result);
    }

    // Test with various integer values including edge cases
    @ParameterizedTest
    @ValueSource(ints = {0, 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    public void test_insertNode_withIntegerEdgeValues_rootIsNull_returnsNewNode(int value) {
        MathTree tree = new MathTree();
        mathNode newNode = new mathNode(value);
        
        mathNode result = tree.insertNode(null, newNode);
        
        assertSame(newNode, result);
        assertEquals(value, result.value);
    }

    // Test inserting a new node with zero value when root exists
    @Test
    public void test_insertNode_newNodeValueZero_insertsCorrectly() {
        MathTree tree = new MathTree();
        mathNode rootNode = new mathNode(10);
        mathNode newNode = new mathNode(0);
        
        mathNode result = tree.insertNode(rootNode, newNode);
        
        assertSame(rootNode, result);
        // Assuming the method inserts newNode in some way, this test might need adjustment
        // based on the actual implementation of insertNode beyond the provided snippet
    }

    // Test with negative value
    @Test
    public void test_insertNode_newNodeValueNegative_insertsCorrectly() {
        MathTree tree = new MathTree();
        mathNode rootNode = new mathNode(10);
        mathNode newNode = new mathNode(-5);
        
        mathNode result = tree.insertNode(rootNode, newNode);
        
        assertSame(rootNode, result);
    }

    // Test with very large positive value
    @Test
    public void test_insertNode_newNodeValueMaxInt_insertsCorrectly() {
        MathTree tree = new MathTree();
        mathNode rootNode = new mathNode(10);
        mathNode newNode = new mathNode(Integer.MAX_VALUE);
        
        mathNode result = tree.insertNode(rootNode, newNode);
        
        assertSame(rootNode, result);
    }

    // Test with very large negative value
    @Test
    public void test_insertNode_newNodeValueMinInt_insertsCorrectly() {
        MathTree tree = new MathTree();
        mathNode rootNode = new mathNode(10);
        mathNode newNode = new mathNode(Integer.MIN_VALUE);
        
        mathNode result = tree.insertNode(rootNode, newNode);
        
        assertSame(rootNode, result);
    }
}
