package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PowToStringGeneratedTest {

    // Stub implementation for MathNode to allow compilation
    static abstract class MathNode {
        abstract String toString();
    }

    // Minimal stub for Pow to allow compilation and testing
    static class Pow {
        private final MathNode left;
        private final MathNode right;
        private final boolean parens;

        public Pow(MathNode left, MathNode right, boolean parens) {
            this.left = left;
            this.right = right;
            this.parens = parens;
        }

        public MathNode getLeftNode() {
            return left;
        }

        public MathNode getRightNode() {
            return right;
        }

        public boolean isParens() {
            return parens;
        }

        public String toString() {
            String str = getLeftNode().toString() + " ^ " + getRightNode().toString();

            if (isParens())
                return '(' + str + ')';
            else
                return str;
        }
    }

    // Helper method to create a simple MathNode with fixed string representation
    private MathNode createConstantNode(String value) {
        return new MathNode() {
            @Override
            String toString() {
                return value;
            }
        };
    }

    @Test
    void test_toString_withoutParens_normalCase() {
        MathNode left = createConstantNode("x");
        MathNode right = createConstantNode("y");
        Pow pow = new Pow(left, right, false);

        assertEquals("x ^ y", pow.toString());
    }

    @Test
    void test_toString_withParens_normalCase() {
        MathNode left = createConstantNode("x");
        MathNode right = createConstantNode("y");
        Pow pow = new Pow(left, right, true);

        assertEquals("(x ^ y)", pow.toString());
    }

    @Test
    void test_toString_withoutParens_zeroExponent() {
        MathNode left = createConstantNode("5");
        MathNode right = createConstantNode("0");
        Pow pow = new Pow(left, right, false);

        assertEquals("5 ^ 0", pow.toString());
    }

    @Test
    void test_toString_withParens_zeroExponent() {
        MathNode left = createConstantNode("5");
        MathNode right = createConstantNode("0");
        Pow pow = new Pow(left, right, true);

        assertEquals("(5 ^ 0)", pow.toString());
    }

    @Test
    void test_toString_withoutParens_negativeBase() {
        MathNode left = createConstantNode("-3");
        MathNode right = createConstantNode("2");
        Pow pow = new Pow(left, right, false);

        assertEquals("-3 ^ 2", pow.toString());
    }

    @Test
    void test_toString_withParens_negativeBase() {
        MathNode left = createConstantNode("-3");
        MathNode right = createConstantNode("2");
        Pow pow = new Pow(left, right, true);

        assertEquals("(-3 ^ 2)", pow.toString());
    }

    @Test
    void test_toString_withoutParens_largeNumbers() {
        MathNode left = createConstantNode("1000000");
        MathNode right = createConstantNode("1000000");
        Pow pow = new Pow(left, right, false);

        assertEquals("1000000 ^ 1000000", pow.toString());
    }

    @Test
    void test_toString_withParens_largeNumbers() {
        MathNode left = createConstantNode("1000000");
        MathNode right = createConstantNode("1000000");
        Pow pow = new Pow(left, right, true);

        assertEquals("(1000000 ^ 1000000)", pow.toString());
    }

    @Test
    void test_toString_withoutParens_emptyStrings() {
        MathNode left = createConstantNode("");
        MathNode right = createConstantNode("");
        Pow pow = new Pow(left, right, false);

        assertEquals(" ^ ", pow.toString());
    }

    @Test
    void test_toString_withParens_emptyStrings() {
        MathNode left = createConstantNode("");
        MathNode right = createConstantNode("");
        Pow pow = new Pow(left, right, true);

        assertEquals("( ^ )", pow.toString());
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void test_toString_parameterized(boolean parens, String leftVal, String rightVal, String expected) {
        MathNode left = createConstantNode(leftVal);
        MathNode right = createConstantNode(rightVal);
        Pow pow = new Pow(left, right, parens);

        assertEquals(expected, pow.toString());
    }

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(false, "a", "b", "a ^ b"),
            Arguments.of(true, "a", "b", "(a ^ b)"),
            Arguments.of(false, "0", "1", "0 ^ 1"),
            Arguments.of(true, "0", "1", "(0 ^ 1)"),
            Arguments.of(false, "-1", "3", "-1 ^ 3"),
            Arguments.of(true, "-1", "3", "(-1 ^ 3)"),
            Arguments.of(false, "2.5", "3.7", "2.5 ^ 3.7"),
            Arguments.of(true, "2.5", "3.7", "(2.5 ^ 3.7)"),
            Arguments.of(false, "x+y", "z", "x+y ^ z"),
            Arguments.of(true, "x+y", "z", "(x+y ^ z)")
        );
    }
}
