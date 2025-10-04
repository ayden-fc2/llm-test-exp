package mathTree;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import mathTree.MathTree;

public class MathTreeCloneGeneratedTest {

    private MathTree mathTree;

    @BeforeEach
    public void setUp() {
        mathTree = new MathTree();
    }

    @Test
    public void test_clone_returnsNewInstance() {
        MathTree clonedTree = (MathTree) mathTree.clone();
        assertNotNull(clonedTree);
        assertNotSame(mathTree, clonedTree);
    }

    @Test
    public void test_clone_preservesRootNode() {
        MathTree clonedTree = (MathTree) mathTree.clone();
        // Assuming rootNode is accessible for comparison
        assertEquals(mathTree.rootNode, clonedTree.rootNode);
    }

    @Test
    public void test_clone_createsNewNodeFactory() {
        MathTree clonedTree = (MathTree) mathTree.clone();
        // Assuming nodeFactory is accessible for comparison
        assertNotSame(mathTree.nodeFactory, clonedTree.nodeFactory);
    }

    @Test
    public void test_clone_withNullRootNode() {
        mathTree.rootNode = null;
        MathTree clonedTree = (MathTree) mathTree.clone();
        assertNull(clonedTree.rootNode);
    }

    @Test
    public void test_clone_withNullNodeFactory() {
        mathTree.nodeFactory = null;
        MathTree clonedTree = (MathTree) mathTree.clone();
        assertNull(clonedTree.nodeFactory);
    }

    @Test
    public void test_clone_returnsCorrectType() {
        Object clonedObject = mathTree.clone();
        assertTrue(clonedObject instanceof MathTree);
    }
}
