package mathTree;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.LinkedList;
import mathTree.MathTree;
import mathTree.mathNode;

public class MathTreeBuildTreeGeneratedTest {

    // Helper method to create a MathTree instance using reflection since buildTree is private
    private MathTree createMathTree() throws Exception {
        return MathTree.class.getDeclaredConstructor().newInstance();
    }

    // Helper method to invoke the private buildTree method
    private mathNode invokeBuildTree(MathTree tree, LinkedList<String> tokens, boolean isParens) throws Exception {
        var method = MathTree.class.getDeclaredMethod("buildTree", LinkedList.class, boolean.class);
        method.setAccessible(true);
        return (mathNode) method.invoke(tree, tokens, isParens);
    }

    @Test
    public void test_buildTree_emptyTokens_returnsNull() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNull(result);
    }

    @Test
    public void test_buildTree_singleNumber_returnsNumberNode() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("42");
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertEquals(42, ((mathNode.Number)result).getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "1", "2147483647", "-2147483648"})
    public void test_buildTree_integerEdgeCases_returnsNumberNode(String value) throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add(value);
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertEquals(Integer.parseInt(value), ((mathNode.Number)result).getValue());
    }

    @Test
    public void test_buildTree_simpleAddition_returnsAddNode() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("1");
        tokens.add("+");
        tokens.add("2");
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Add);
        mathNode.Add addNode = (mathNode.Add) result;
        assertEquals(1, ((mathNode.Number)addNode.getLeft()).getValue());
        assertEquals(2, ((mathNode.Number)addNode.getRight()).getValue());
    }

    @Test
    public void test_buildTree_simpleSubtraction_returnsSubNode() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("5");
        tokens.add("-");
        tokens.add("3");
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Sub);
        mathNode.Sub subNode = (mathNode.Sub) result;
        assertEquals(5, ((mathNode.Number)subNode.getLeft()).getValue());
        assertEquals(3, ((mathNode.Number)subNode.getRight()).getValue());
    }

    @Test
    public void test_buildTree_simpleMultiplication_returnsMulNode() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("4");
        tokens.add("*");
        tokens.add("6");
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Mul);
        mathNode.Mul mulNode = (mathNode.Mul) result;
        assertEquals(4, ((mathNode.Number)mulNode.getLeft()).getValue());
        assertEquals(6, ((mathNode.Number)mulNode.getRight()).getValue());
    }

    @Test
    public void test_buildTree_simpleDivision_returnsDivNode() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("8");
        tokens.add("/");
        tokens.add("2");
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Div);
        mathNode.Div divNode = (mathNode.Div) result;
        assertEquals(8, ((mathNode.Number)divNode.getLeft()).getValue());
        assertEquals(2, ((mathNode.Number)divNode.getRight()).getValue());
    }

    @Test
    public void test_buildTree_nestedParentheses_returnsCorrectTree() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("(");
        tokens.add("1");
        tokens.add("+");
        tokens.add("2");
        tokens.add(")");
        tokens.add("*");
        tokens.add("3");
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Mul);
        mathNode.Mul mulNode = (mathNode.Mul) result;
        assertTrue(mulNode.getLeft() instanceof mathNode.Add);
        mathNode.Add addNode = (mathNode.Add) mulNode.getLeft();
        assertEquals(1, ((mathNode.Number)addNode.getLeft()).getValue());
        assertEquals(2, ((mathNode.Number)addNode.getRight()).getValue());
        assertEquals(3, ((mathNode.Number)mulNode.getRight()).getValue());
    }

    @Test
    public void test_buildTree_complexExpression_returnsCorrectTree() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("10");
        tokens.add("+");
        tokens.add("2");
        tokens.add("*");
        tokens.add("3");
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Add);
        mathNode.Add addNode = (mathNode.Add) result;
        assertEquals(10, ((mathNode.Number)addNode.getLeft()).getValue());
        assertTrue(addNode.getRight() instanceof mathNode.Mul);
        mathNode.Mul mulNode = (mathNode.Mul) addNode.getRight();
        assertEquals(2, ((mathNode.Number)mulNode.getLeft()).getValue());
        assertEquals(3, ((mathNode.Number)mulNode.getRight()).getValue());
    }

    @Test
    public void test_buildTree_negativeNumber_returnsNumberNode() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("-42");
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertEquals(-42, ((mathNode.Number)result).getValue());
    }

    @Test
    public void test_buildTree_withUnaryMinus_returnsCorrectTree() throws Exception {
        MathTree tree = createMathTree();
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("-");
        tokens.add("5");
        mathNode result = invokeBuildTree(tree, tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Neg);
        mathNode.Neg negNode = (mathNode.Neg) result;
        assertEquals(5, ((mathNode.Number)negNode.getChild()).getValue());
    }
}
