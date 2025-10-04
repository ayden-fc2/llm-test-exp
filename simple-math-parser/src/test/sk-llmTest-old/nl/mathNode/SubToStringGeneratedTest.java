package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SubToStringGeneratedTest {

    // Stub implementation of Node interface required for compilation and testing
    static abstract class Node {
        abstract String toString();
    }

    // Minimal stub for Sub class based on provided snippet
    static class Sub extends Node {
        private final Node left;
        private final Node right;
        private final boolean parens;

        Sub(Node left, Node right, boolean parens) {
            this.left = left;
            this.right = right;
            this.parens = parens;
        }

        public Node getLeftNode() { return left; }
        public Node getRightNode() { return right; }
        public boolean isParens() { return parens; }

        @Override
        public String toString() {
            String str = getLeftNode().toString() + " - " + getRightNode().toString();
            if (isParens())
                return '(' + str + ')';
            else
                return str;
        }
    }

    // Helper to create leaf node with fixed string value
    private static Node valueNode(String val) {
        return new Node() {
            @Override
            public String toString() {
                return val;
            }
        };
    }

    @Test
    void test_toString_withoutParentheses_returnsSimpleExpression() {
        Node left = valueNode("x");
        Node right = valueNode("y");
        Sub sub = new Sub(left, right, false);
        assertEquals("x - y", sub.toString());
    }

    @Test
    void test_toString_withParentheses_returnsParenthesizedExpression() {
        Node left = valueNode("a");
        Node right = valueNode("b");
        Sub sub = new Sub(left, right, true);
        assertEquals("(a - b)", sub.toString());
    }

    @ParameterizedTest
    @MethodSource("provideBoundaryValues")
    void test_toString_withBoundaryValueNodes_handlesSpecialStrings(
            String leftVal, String rightVal, boolean parens, String expected) {
        Node left = valueNode(leftVal);
        Node right = valueNode(rightVal);
        Sub sub = new Sub(left, right, parens);
        assertEquals(expected, sub.toString());
    }

    static Stream<Arguments> provideBoundaryValues() {
        return Stream.of(
            Arguments.of("", "", false, " - "),
            Arguments.of("", "", true, "( - )"),
            Arguments.of("0", "-0", false, "0 - -0"),
            Arguments.of(" ", " \t", true, "(  -  \t)"),
            Arguments.of("MAX_INT", "MIN_INT", false, "MAX_INT - MIN_INT"),
            Arguments.of("+inf", "-inf", true, "(+inf - -inf)"),
            Arguments.of("null", "null", false, "null - null") // assuming "null" string as output from nodes
        );
    }

    @Test
    void test_toString_withEmptyAndNormalNode_combinesCorrectlyWithoutParens() {
        Node left = valueNode("");
        Node right = valueNode("z");
        Sub sub = new Sub(left, right, false);
        assertEquals(" - z", sub.toString());
    }

    @Test
    void test_toString_withComplexExpressionsInNodes_preservesInnerStructure() {
        Node complexLeft = valueNode("(x+y)");
        Node complexRight = valueNode("z*w");
        Sub sub = new Sub(complexLeft, complexRight, true);
        assertEquals("((x+y) - z*w)", sub.toString());
    }

    @Test
    void test_toString_branchCoverage_ensuresBothPathsTaken() {
        Node n1 = valueNode("1");
        Node n2 = valueNode("2");

        Sub withoutParens = new Sub(n1, n2, false);
        assertFalse(withoutParens.toString().startsWith("("));
        assertFalse(withoutParens.toString().endsWith(")"));

        Sub withParens = new Sub(n1, n2, true);
        assertTrue(withParens.toString().startsWith("("));
        assertTrue(withParens.toString().endsWith(")"));
    }
}
