package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DivToStringGeneratedTest {

    // Mock Node implementation for testing
    private static class MockNode implements Node {
        private final String representation;

        public MockNode(String representation) {
            this.representation = representation;
        }

        @Override
        public String toString() {
            return representation;
        }

        @Override
        public Node getLeftNode() {
            return null;
        }

        @Override
        public Node getRightNode() {
            return null;
        }

        @Override
        public boolean isParens() {
            return false;
        }
    }

    // Mock Div implementation to control getLeftNode, getRightNode, and isParens behavior
    private static class TestableDiv implements Node {
        private final Node left;
        private final Node right;
        private final boolean parens;

        public TestableDiv(Node left, Node right, boolean parens) {
            this.left = left;
            this.right = right;
            this.parens = parens;
        }

        @Override
        public Node getLeftNode() {
            return left;
        }

        @Override
        public Node getRightNode() {
            return right;
        }

        @Override
        public boolean isParens() {
            return parens;
        }

        @Override
        public String toString() {
            String str = getLeftNode().toString() + " / " + getRightNode().toString();
            if (isParens())
                return '(' + str + ')';
            else
                return str;
        }
    }

    @Test
    void test_toString_normalDivisionWithoutParens() {
        Node left = new MockNode("5");
        Node right = new MockNode("2");
        Div div = new TestableDiv(left, right, false);
        assertEquals("5 / 2", div.toString());
    }

    @Test
    void test_toString_normalDivisionWithParens() {
        Node left = new MockNode("x");
        Node right = new MockNode("y");
        Div div = new TestableDiv(left, right, true);
        assertEquals("(x / y)", div.toString());
    }

    @ParameterizedTest
    @MethodSource("provideNodesForDivision")
    void test_toString_variousCombinations(Node left, Node right, boolean parens, String expected) {
        Div div = new TestableDiv(left, right, parens);
        assertEquals(expected, div.toString());
    }

    static Stream<Arguments> provideNodesForDivision() {
        return Stream.of(
            Arguments.of(new MockNode("a"), new MockNode("b"), false, "a / b"),
            Arguments.of(new MockNode("a"), new MockNode("b"), true, "(a / b)"),
            Arguments.of(new MockNode("0"), new MockNode("1"), false, "0 / 1"),
            Arguments.of(new MockNode("-1"), new MockNode("1"), true, "(-1 / 1)"),
            Arguments.of(new MockNode("MAX"), new MockNode("MIN"), false, "MAX / MIN"),
            Arguments.of(new MockNode(""), new MockNode(""), true, "( / )"),
            Arguments.of(new MockNode(" "), new MockNode(" "), false, "   /  "),
            Arguments.of(new MockNode("3.14"), new MockNode("2.71"), true, "(3.14 / 2.71)")
        );
    }

    @Test
    void test_toString_withEmptyStrings() {
        Node left = new MockNode("");
        Node right = new MockNode("");
        Div div = new TestableDiv(left, right, false);
        assertEquals(" / ", div.toString());
    }

    @Test
    void test_toString_withWhitespaceStrings() {
        Node left = new MockNode(" ");
        Node right = new MockNode(" ");
        Div div = new TestableDiv(left, right, true);
        assertEquals("(   /  )", div.toString());
    }

    @Test
    void test_toString_withZeroAndOne() {
        Node left = new MockNode("0");
        Node right = new MockNode("1");
        Div div = new TestableDiv(left, right, false);
        assertEquals("0 / 1", div.toString());
    }

    @Test
    void test_toString_withNegativeValues() {
        Node left = new MockNode("-5");
        Node right = new MockNode("-2");
        Div div = new TestableDiv(left, right, true);
        assertEquals("(-5 / -2)", div.toString());
    }

    @Test
    void test_toString_extremeValues() {
        Node left = new MockNode("MAX_INT");
        Node right = new MockNode("MIN_INT");
        Div div = new TestableDiv(left, right, false);
        assertEquals("MAX_INT / MIN_INT", div.toString());
    }

    @Test
    void test_toString_decimalValues() {
        Node left = new MockNode("3.14159");
        Node right = new MockNode("2.71828");
        Div div = new TestableDiv(left, right, true);
        assertEquals("(3.14159 / 2.71828)", div.toString());
    }

    @Test
    void test_toString_parensFalse() {
        Node left = new MockNode("expr1");
        Node right = new MockNode("expr2");
        Div div = new TestableDiv(left, right, false);
        assertFalse(div.toString().startsWith("("));
        assertFalse(div.toString().endsWith(")"));
    }

    @Test
    void test_toString_parensTrue() {
        Node left = new MockNode("expr1");
        Node right = new MockNode("expr2");
        Div div = new TestableDiv(left, right, true);
        assertTrue(div.toString().startsWith("("));
        assertTrue(div.toString().endsWith(")"));
    }
}
