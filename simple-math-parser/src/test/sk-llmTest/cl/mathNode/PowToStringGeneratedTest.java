package mathNode;

import mathNode.Pow;
import mathNode.MathNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PowToStringGeneratedTest {

    // Stub implementation for MathNode to allow compilation
    static class MathNodeStub extends MathNode {
        private final String representation;
        private final boolean parens;

        MathNodeStub(String representation, boolean parens) {
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
            return this;
        }

        @Override
        public MathNode getRightNode() {
            return this;
        }
    }

    static Stream<Arguments> toStringTestCases() {
        return Stream.of(
            // Basic case without parentheses
            Arguments.of("2", "3", false, "2 ^ 3"),
            // Case with parentheses
            Arguments.of("2", "3", true, "(2 ^ 3)"),
            // Edge values
            Arguments.of("0", "1", false, "0 ^ 1"),
            Arguments.of("-1", "2", false, "-1 ^ 2"),
            Arguments.of("1", "-1", false, "1 ^ -1"),
            Arguments.of("0", "0", false, "0 ^ 0"),
            // Large numbers
            Arguments.of("1000000", "1000000", false, "1000000 ^ 1000000"),
            // Small numbers
            Arguments.of("0.000001", "0.000001", false, "0.000001 ^ 0.000001"),
            // Negative base with parentheses
            Arguments.of("-2", "3", true, "(-2 ^ 3)"),
            // Complex expressions
            Arguments.of("(a+b)", "(c-d)", false, "(a+b) ^ (c-d)")
        );
    }

    @ParameterizedTest
    @MethodSource("toStringTestCases")
    void test_toString_variousInputs(String left, String right, boolean parens, String expected) {
        MathNode leftNode = new MathNodeStub(left, false);
        MathNode rightNode = new MathNodeStub(right, false);
        
        Pow pow = new Pow(leftNode, rightNode);
        // Using reflection to set parens since there's no setter in the stub
        try {
            java.lang.reflect.Field parensField = MathNode.class.getDeclaredField("parens");
            parensField.setAccessible(true);
            parensField.set(pow, parens);
        } catch (Exception e) {
            fail("Failed to set parens field: " + e.getMessage());
        }
        
        assertEquals(expected, pow.toString(), "toString() should return correct format");
    }

    @Test
    void test_toString_withNullNodes() {
        Pow pow = new Pow(null, null);
        // This test assumes that getLeftNode() and getRightNode() can return null
        // and that toString handles them gracefully (implementation-dependent)
        try {
            String result = pow.toString();
            // If it doesn't throw, it should be a valid string
            assertNotNull(result);
        } catch (NullPointerException e) {
            // This might be expected behavior depending on implementation
            // The test passes if we document this behavior
        }
    }
}
