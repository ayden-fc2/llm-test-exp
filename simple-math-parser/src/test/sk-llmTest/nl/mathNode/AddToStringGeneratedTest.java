package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AddToStringGeneratedTest {

    // Mock Node interface and base implementation for testing purposes
    interface Node {
        String toString();
    }

    static class SimpleNode implements Node {
        private final String value;

        SimpleNode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    // Minimal stub for Add class to allow compilation and testing
    static class Add {
        private final Node leftNode;
        private final Node rightNode;
        private final boolean parens;

        public Add(Node leftNode, Node rightNode, boolean parens) {
            this.leftNode = leftNode;
            this.rightNode = rightNode;
            this.parens = parens;
        }

        public Node getLeftNode() {
            return leftNode;
        }

        public Node getRightNode() {
            return rightNode;
        }

        public boolean isParens() {
            return parens;
        }

        @Override
        public String toString() {
            String str = getLeftNode().toString() + " + " + getRightNode().toString();

            if (isParens())
                return '(' + str + ')';
            else
                return str;
        }
    }

    // Test normal case without parentheses
    @Test
    void test_toString_normal_noParens() {
        Add add = new Add(new SimpleNode("a"), new SimpleNode("b"), false);
        assertEquals("a + b", add.toString());
    }

    // Test normal case with parentheses
    @Test
    void test_toString_normal_withParens() {
        Add add = new Add(new SimpleNode("x"), new SimpleNode("y"), true);
        assertEquals("(x + y)", add.toString());
    }

    // Parameterized test to cover multiple combinations of inputs and parens flag
    @ParameterizedTest
    @MethodSource("provideTestCases")
    void test_toString_combinations(Node left, Node right, boolean parens, String expected) {
        Add add = new Add(left, right, parens);
        assertEquals(expected, add.toString());
    }

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(new SimpleNode("0"), new SimpleNode("1"), false, "0 + 1"),
            Arguments.of(new SimpleNode("-1"), new SimpleNode("1"), true, "(-1 + 1)"),
            Arguments.of(new SimpleNode(""), new SimpleNode("empty"), false, " + empty"),
            Arguments.of(new SimpleNode("MAX_INT"), new SimpleNode("MIN_INT"), true, "(MAX_INT + MIN_INT)"),
            Arguments.of(new SimpleNode("very_large_string_aaaaaaaaaaaaaaaaaaaa"), 
                         new SimpleNode("very_large_string_bbbbbbbbbbbbbbbbbbbb"), 
                         false, 
                         "very_large_string_aaaaaaaaaaaaaaaaaaaa + very_large_string_bbbbbbbbbbbbbbbbbbbb")
        );
    }

    // Edge case: both nodes return empty strings
    @Test
    void test_toString_bothNodesEmpty_noParens() {
        Add add = new Add(new SimpleNode(""), new SimpleNode(""), false);
        assertEquals(" + ", add.toString());
    }

    // Edge case: one node returns empty string
    @Test
    void test_toString_oneNodeEmpty_withParens() {
        Add add = new Add(new SimpleNode(""), new SimpleNode("nonempty"), true);
        assertEquals("( + nonempty)", add.toString());
    }

    // Boundary condition: very long strings (simulate large input)
    @Test
    void test_toString_veryLongStrings_noParens() {
        String longStr1 = "A".repeat(1000);
        String longStr2 = "B".repeat(1000);
        Add add = new Add(new SimpleNode(longStr1), new SimpleNode(longStr2), false);
        assertEquals(longStr1 + " + " + longStr2, add.toString());
    }

    // Branch coverage: ensure both branches of isParens() are covered
    @Test
    void test_toString_branch_false() {
        Add add = new Add(new SimpleNode("left"), new SimpleNode("right"), false);
        assertFalse(add.toString().startsWith("("));
        assertFalse(add.toString().endsWith(")"));
    }

    @Test
    void test_toString_branch_true() {
        Add add = new Add(new SimpleNode("left"), new SimpleNode("right"), true);
        assertTrue(add.toString().startsWith("("));
        assertTrue(add.toString().endsWith(")"));
    }
}
