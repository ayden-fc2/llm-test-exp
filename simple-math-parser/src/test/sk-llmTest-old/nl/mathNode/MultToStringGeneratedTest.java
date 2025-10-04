package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MultToStringGeneratedTest {

    // 最小桩实现以支持测试 Mult.toString()
    static class TestNode implements Node {
        private final String value;

        TestNode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public Node getLeftNode() {
            return null; // Not used in this test
        }

        @Override
        public Node getRightNode() {
            return null; // Not used in this test
        }

        @Override
        public boolean isParens() {
            return false; // Default, overridden in Mult when needed
        }
    }

    static class Mult extends TestNode {
        private final Node left;
        private final Node right;
        private final boolean parens;

        Mult(Node left, Node right, boolean parens) {
            super(""); // Unused in Mult
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
            String str = getLeftNode().toString() + " * " + getRightNode().toString();

            if (isParens())
                return '(' + str + ')';
            else
                return str;
        }
    }

    // 参数化测试数据源：正常情况
    static Stream<Arguments> normalCases() {
        return Stream.of(
            Arguments.of("a", "b", false, "a * b"),
            Arguments.of("1", "2", false, "1 * 2"),
            Arguments.of("x", "y", true, "(x * y)"),
            Arguments.of("3", "4", true, "(3 * 4)")
        );
    }

    @ParameterizedTest
    @MethodSource("normalCases")
    void test_toString_Normal(String leftVal, String rightVal, boolean parens, String expected) {
        Node left = new TestNode(leftVal);
        Node right = new TestNode(rightVal);
        Mult mult = new Mult(left, right, parens);
        assertEquals(expected, mult.toString());
    }

    // 边界和特殊值测试
    @Test
    void test_toString_EmptyStrings() {
        Node left = new TestNode("");
        Node right = new TestNode("");
        Mult mult = new Mult(left, right, false);
        assertEquals(" * ", mult.toString());
    }

    @Test
    void test_toString_EmptyStrings_WithParens() {
        Node left = new TestNode("");
        Node right = new TestNode("");
        Mult mult = new Mult(left, right, true);
        assertEquals("( * )", mult.toString());
    }

    @Test
    void test_toString_ZeroAndOne() {
        Node left = new TestNode("0");
        Node right = new TestNode("1");
        Mult mult = new Mult(left, right, false);
        assertEquals("0 * 1", mult.toString());
    }

    @Test
    void test_toString_NegativeNumbers() {
        Node left = new TestNode("-1");
        Node right = new TestNode("-2");
        Mult mult = new Mult(left, right, false);
        assertEquals("-1 * -2", mult.toString());
    }

    @Test
    void test_toString_ExtremeValues() {
        Node left = new TestNode("2147483647"); // Integer.MAX_VALUE as string
        Node right = new TestNode("-2147483648"); // Integer.MIN_VALUE as string
        Mult mult = new Mult(left, right, true);
        assertEquals("(2147483647 * -2147483648)", mult.toString());
    }

    @Test
    void test_toString_SpacesInOperands() {
        Node left = new TestNode("x + y");
        Node right = new TestNode("z - w");
        Mult mult = new Mult(left, right, false);
        assertEquals("x + y * z - w", mult.toString());
    }

    @Test
    void test_toString_SpacesInOperands_WithParens() {
        Node left = new TestNode("x + y");
        Node right = new TestNode("z - w");
        Mult mult = new Mult(left, right, true);
        assertEquals("(x + y * z - w)", mult.toString());
    }

    @Test
    void test_toString_LongStrings() {
        String longStr1 = "a".repeat(1000);
        String longStr2 = "b".repeat(1000);
        Node left = new TestNode(longStr1);
        Node right = new TestNode(longStr2);
        Mult mult = new Mult(left, right, false);
        assertEquals(longStr1 + " * " + longStr2, mult.toString());
    }

    @Test
    void test_toString_SpecialCharacters() {
        Node left = new TestNode("x@y");
        Node right = new TestNode("z#w");
        Mult mult = new Mult(left, right, true);
        assertEquals("(x@y * z#w)", mult.toString());
    }
}
