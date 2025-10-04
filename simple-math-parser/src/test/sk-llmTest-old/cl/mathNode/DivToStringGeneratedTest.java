package mathNode;

import mathNode.Div;
import mathNode.MathNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DivToStringGeneratedTest {

    // Stub implementation for MathNode to allow compilation
    static class MathNodeStub extends MathNode {
        private final String representation;
        private final boolean parens;

        public MathNodeStub(String representation, boolean parens) {
            this.representation = representation;
            this.parens = parens;
        }

        @Override
        public String toString() {
            return representation;
        }

        @Override
        public boolean isParens() {
            return parens;
        }

        @Override
        public MathNode getLeftNode() {
            return null;
        }

        @Override
        public MathNode getRightNode() {
            return null;
        }
    }

    static class TestableDiv extends Div {
        private final MathNode left;
        private final MathNode right;
        private final boolean parens;

        TestableDiv(MathNode left, MathNode right, boolean parens) {
            this.left = left;
            this.right = right;
            this.parens = parens;
        }

        @Override
        public MathNode getLeftNode() {
            return left;
        }

        @Override
        public MathNode getRightNode() {
            return right;
        }

        @Override
        public boolean isParens() {
            return parens;
        }
    }

    @Test
    void test_toString_withoutParens_simpleNodes() {
        MathNode left = new MathNodeStub("5", false);
        MathNode right = new MathNodeStub("3", false);
        Div div = new TestableDiv(left, right, false);

        assertEquals("5 / 3", div.toString());
    }

    @Test
    void test_toString_withParens_simpleNodes() {
        MathNode left = new MathNodeStub("5", false);
        MathNode right = new MathNodeStub("3", false);
        Div div = new TestableDiv(left, right, true);

        assertEquals("(5 / 3)", div.toString());
    }

    @Test
    void test_toString_withoutParens_complexNodes() {
        MathNode left = new MathNodeStub("(2 + 3)", true);
        MathNode right = new MathNodeStub("x", false);
        Div div = new TestableDiv(left, right, false);

        assertEquals("(2 + 3) / x", div.toString());
    }

    @Test
    void test_toString_withParens_complexNodes() {
        MathNode left = new MathNodeStub("(2 + 3)", true);
        MathNode right = new MathNodeStub("x", false);
        Div div = new TestableDiv(left, right, true);

        assertEquals("((2 + 3) / x)", div.toString());
    }

    @Test
    void test_toString_withoutParens_emptyStrings() {
        MathNode left = new MathNodeStub("", false);
        MathNode right = new MathNodeStub("", false);
        Div div = new TestableDiv(left, right, false);

        assertEquals(" / ", div.toString());
    }

    @Test
    void test_toString_withParens_emptyStrings() {
        MathNode left = new MathNodeStub("", false);
        MathNode right = new MathNodeStub("", false);
        Div div = new TestableDiv(left, right, true);

        assertEquals("( / )", div.toString());
    }

    @ParameterizedTest
    @MethodSource("provideEdgeCases")
    void test_toString_edgeCases(String leftStr, String rightStr, boolean parens, String expected) {
        MathNode left = new MathNodeStub(leftStr, false);
        MathNode right = new MathNodeStub(rightStr, false);
        Div div = new TestableDiv(left, right, parens);

        assertEquals(expected, div.toString());
    }

    static Stream<Arguments> provideEdgeCases() {
        return Stream.of(
            Arguments.of("0", "1", false, "0 / 1"),
            Arguments.of("-1", "1", false, "-1 / 1"),
            Arguments.of("2147483647", "1", false, "2147483647 / 1"),
            Arguments.of("-2147483648", "1", false, "-2147483648 / 1"),
            Arguments.of("1.7976931348623157E308", "1", false, "1.7976931348623157E308 / 1"),
            Arguments.of("4.9E-324", "1", false, "4.9E-324 / 1"),
            Arguments.of("0", "1", true, "(0 / 1)"),
            Arguments.of("-1", "1", true, "(-1 / 1)"),
            Arguments.of("2147483647", "1", true, "(2147483647 / 1)"),
            Arguments.of("-2147483648", "1", true, "(-2147483648 / 1)"),
            Arguments.of("1.7976931348623157E308", "1", true, "(1.7976931348623157E308 / 1)"),
            Arguments.of("4.9E-324", "1", true, "(4.9E-324 / 1)")
        );
    }
}
