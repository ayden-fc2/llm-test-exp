package mathTree;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.LinkedList;
import mathTree.MathTree;
import mathTree.mathNode;

public class MathTreeBuildTreeGeneratedTest {

    // Helper method to create a MathTree instance for testing private method
    private MathTree createMathTree() {
        return new MathTree();
    }

    // Helper method to invoke the private buildTree method using reflection
    private mathNode invokeBuildTree(LinkedList<String> tokens, boolean isParens) throws Exception {
        MathTree tree = createMathTree();
        java.lang.reflect.Method method = MathTree.class.getDeclaredMethod("buildTree", LinkedList.class, boolean.class);
        method.setAccessible(true);
        return (mathNode) method.invoke(tree, tokens, isParens);
    }

    @Test
    public void test_buildTree_emptyList_returnsNull() throws Exception {
        LinkedList<String> tokens = new LinkedList<>();
        mathNode result = invokeBuildTree(tokens, false);
        assertNull(result);
    }

    @Test
    public void test_buildTree_singleNumber_createsNumberNode() throws Exception {
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("42");
        mathNode result = invokeBuildTree(tokens, false);
        assertNotNull(result);
        // Assuming mathNode has appropriate structure; adjust based on actual implementation
        assertEquals(42, ((mathNode.Number)result).value);
    }

    @Test
    public void test_buildTree_negativeNumber_handlesCorrectly() throws Exception {
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("-10");
        mathNode result = invokeBuildTree(tokens, false);
        assertNotNull(result);
        assertEquals(-10, ((mathNode.Number)result).value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "-1", "2147483647", "-2147483648"})
    public void test_buildTree_integerBoundaryValues_handlesCorrectly(String value) throws Exception {
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add(value);
        mathNode result = invokeBuildTree(tokens, false);
        assertNotNull(result);
        assertEquals(Integer.parseInt(value), ((mathNode.Number)result).value);
    }

    @Test
    public void test_buildTree_simpleAddition_createsAdditionNode() throws Exception {
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("5");
        tokens.add("+");
        tokens.add("3");
        mathNode result = invokeBuildTree(tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Addition);
        mathNode.Addition addNode = (mathNode.Addition) result;
        assertEquals(5, ((mathNode.Number)addNode.left).value);
        assertEquals(3, ((mathNode.Number)addNode.right).value);
    }

    @Test
    public void test_buildTree_simpleSubtraction_createsSubtractionNode() throws Exception {
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("10");
        tokens.add("-");
        tokens.add("4");
        mathNode result = invokeBuildTree(tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Subtraction);
        mathNode.Subtraction subNode = (mathNode.Subtraction) result;
        assertEquals(10, ((mathNode.Number)subNode.left).value);
        assertEquals(4, ((mathNode.Number)subNode.right).value);
    }

    @Test
    public void test_buildTree_simpleMultiplication_createsMultiplicationNode() throws Exception {
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("6");
        tokens.add("*");
        tokens.add("7");
        mathNode result = invokeBuildTree(tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Multiplication);
        mathNode.Multiplication mulNode = (mathNode.Multiplication) result;
        assertEquals(6, ((mathNode.Number)mulNode.left).value);
        assertEquals(7, ((mathNode.Number)mulNode.right).value);
    }

    @Test
    public void test_buildTree_simpleDivision_createsDivisionNode() throws Exception {
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("20");
        tokens.add("/");
        tokens.add("5");
        mathNode result = invokeBuildTree(tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Division);
        mathNode.Division divNode = (mathNode.Division) result;
        assertEquals(20, ((mathNode.Number)divNode.left).value);
        assertEquals(5, ((mathNode.Number)divNode.right).value);
    }

    @Test
    public void test_buildTree_parenthesesWithSingleNumber_preservesGrouping() throws Exception {
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("(");
        tokens.add("8");
        tokens.add(")");
        mathNode result = invokeBuildTree(tokens, true);
        assertNotNull(result);
        assertEquals(8, ((mathNode.Number)result).value);
    }

    @Test
    public void test_buildTree_nestedParentheses_buildsCorrectStructure() throws Exception {
        // Represents (2 + (3 * 4))
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("(");
        tokens.add("2");
        tokens.add("+");
        tokens.add("(");
        tokens.add("3");
        tokens.add("*");
        tokens.add("4");
        tokens.add(")");
        tokens.add(")");
        mathNode result = invokeBuildTree(tokens, true);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Addition);
        mathNode.Addition addNode = (mathNode.Addition) result;
        assertEquals(2, ((mathNode.Number)addNode.left).value);
        assertTrue(addNode.right instanceof mathNode.Multiplication);
        mathNode.Multiplication mulNode = (mathNode.Multiplication) addNode.right;
        assertEquals(3, ((mathNode.Number)mulNode.left).value);
        assertEquals(4, ((mathNode.Number)mulNode.right).value);
    }

    @Test
    public void test_buildTree_complexExpression_followsOperatorPrecedence() throws Exception {
        // Represents 2 + 3 * 4 => should be 2 + (3 * 4)
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("2");
        tokens.add("+");
        tokens.add("3");
        tokens.add("*");
        tokens.add("4");
        mathNode result = invokeBuildTree(tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Addition);
        mathNode.Addition addNode = (mathNode.Addition) result;
        assertEquals(2, ((mathNode.Number)addNode.left).value);
        assertTrue(addNode.right instanceof mathNode.Multiplication);
        mathNode.Multiplication mulNode = (mathNode.Multiplication) addNode.right;
        assertEquals(3, ((mathNode.Number)mulNode.left).value);
        assertEquals(4, ((mathNode.Number)mulNode.right).value);
    }

    @Test
    public void test_buildTree_leftToRightAssociativity_forSamePrecedence() throws Exception {
        // Represents 10 - 5 - 2 => should be (10 - 5) - 2
        LinkedList<String> tokens = new LinkedList<>();
        tokens.add("10");
        tokens.add("-");
        tokens.add("5");
        tokens.add("-");
        tokens.add("2");
        mathNode result = invokeBuildTree(tokens, false);
        assertNotNull(result);
        assertTrue(result instanceof mathNode.Subtraction);
        mathNode.Subtraction subNode1 = (mathNode.Subtraction) result;
        assertTrue(subNode1.left instanceof mathNode.Subtraction);
        mathNode.Subtraction subNode2 = (mathNode.Subtraction) subNode1.left;
        assertEquals(10, ((mathNode.Number)subNode2.left).value);
        assertEquals(5, ((mathNode.Number)subNode2.right).value);
        assertEquals(2, ((mathNode.Number)subNode1.right).value);
    }
}
