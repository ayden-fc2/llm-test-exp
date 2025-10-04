package mathNode;

import mathNode.Add;
import mathNode.MathNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AddToStringGeneratedTest {

    // Stub implementation for MathNode to enable compilation
    static class MathNodeStub extends MathNode {
        private final String value;
        private final boolean parens;

        MathNodeStub(String value, boolean parens) {
            this.value = value;
            this.parens = parens;
        }

        @Override
        public String toString() {
            return value;
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
                Arguments.of("2", "3", false, "2 + 3"),
                // Case with parentheses
                Arguments.of("2", "3", true, "(2 + 3)"),
                // Negative numbers
                Arguments.of("-5", "10", false, "-5 + 10"),
                // Zero values
                Arguments.of("0", "0", false, "0 + 0"),
                // Large integers
                Arguments.of(String.valueOf(Integer.MAX_VALUE), "1", false, Integer.MAX_VALUE + " + 1"),
                // Small integers
                Arguments.of(String.valueOf(Integer.MIN_VALUE), "-1", false, Integer.MIN_VALUE + " + -1"),
                // Complex expressions (simulated via string values)
                Arguments.of("(a + b)", "c", false, "(a + b) + c"),
                // Nested with parentheses
                Arguments.of("x", "y", true, "(x + y)")
        );
    }

    @ParameterizedTest
    @MethodSource("toStringTestCases")
    void test_toString_variousInputs(String left, String right, boolean parens, String expected) {
        MathNode leftNode = new MathNodeStub(left, false);
        MathNode rightNode = new MathNodeStub(right, false);
        
        Add add = new Add(leftNode, rightNode) {
            @Override
            public boolean isParens() {
                return parens;
            }
        };

        assertEquals(expected, add.toString());
    }

    @Test
    void test_toString_withNullNodes() {
        Add add = new Add(null, null) {
            @Override
            public boolean isParens() {
                return false;
            }
        };

        assertThrows(NullPointerException.class, add::toString);
    }

    @Test
    void test_toString_withParensAndNullNodes() {
        Add add = new Add(null, null) {
            @Override
            public boolean isParens() {
                return true;
            }
        };

        assertThrows(NullPointerException.class, add::toString);
    }
}
