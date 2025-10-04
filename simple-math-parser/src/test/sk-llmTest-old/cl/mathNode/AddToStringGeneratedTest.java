package mathNode;

import mathNode.Add;
import mathNode.MathNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class AddToStringGeneratedTest {

    // Stub implementation for MathNode to allow compilation
    static abstract class MathNodeStub extends MathNode {
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
            return null;
        }

        @Override
        public MathNode getRightNode() {
            return null;
        }
    }

    @Test
    public void test_toString_noParens_returnsSimpleExpression() {
        MathNode left = new MathNodeStub("2", false) {};
        MathNode right = new MathNodeStub("3", false) {};
        Add add = new Add(left, right) {};

        String result = add.toString();

        assertEquals("2 + 3", result);
    }

    @Test
    public void test_toString_withParens_returnsParenthesizedExpression() {
        MathNode left = new MathNodeStub("2", true) {};
        MathNode right = new MathNodeStub("3", true) {};
        Add add = new Add(left, right) {};
        add.setParens(true);

        String result = add.toString();

        assertEquals("(2 + 3)", result);
    }

    @Test
    public void test_toString_zeroValues_noParens() {
        MathNode left = new MathNodeStub("0", false) {};
        MathNode right = new MathNodeStub("0", false) {};
        Add add = new Add(left, right) {};

        String result = add.toString();

        assertEquals("0 + 0", result);
    }

    @Test
    public void test_toString_negativeValues_noParens() {
        MathNode left = new MathNodeStub("-5", false) {};
        MathNode right = new MathNodeStub("-10", false) {};
        Add add = new Add(left, right) {};

        String result = add.toString();

        assertEquals("-5 + -10", result);
    }

    @Test
    public void test_toString_maxIntValues_withParens() {
        MathNode left = new MathNodeStub(String.valueOf(Integer.MAX_VALUE), false) {};
        MathNode right = new MathNodeStub(String.valueOf(Integer.MAX_VALUE), false) {};
        Add add = new Add(left, right) {};
        add.setParens(true);

        String result = add.toString();

        assertEquals("(" + Integer.MAX_VALUE + " + " + Integer.MAX_VALUE + ")", result);
    }

    @Test
    public void test_toString_minIntValues_withParens() {
        MathNode left = new MathNodeStub(String.valueOf(Integer.MIN_VALUE), false) {};
        MathNode right = new MathNodeStub(String.valueOf(Integer.MIN_VALUE), false) {};
        Add add = new Add(left, right) {};
        add.setParens(true);

        String result = add.toString();

        assertEquals("(" + Integer.MIN_VALUE + " + " + Integer.MIN_VALUE + ")", result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "x", "123456789"})
    public void test_toString_edgeCaseStrings_noParens(String value) {
        MathNode left = new MathNodeStub(value, false) {};
        MathNode right = new MathNodeStub(value, false) {};
        Add add = new Add(left, right) {};

        String result = add.toString();

        assertEquals(value + " + " + value, result);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void test_toString_booleanFlagAffectsParens(boolean hasParens) {
        MathNode left = new MathNodeStub("a", false) {};
        MathNode right = new MathNodeStub("b", false) {};
        Add add = new Add(left, right) {};
        add.setParens(hasParens);

        String result = add.toString();

        if (hasParens) {
            assertEquals("(a + b)", result);
        } else {
            assertEquals("a + b", result);
        }
    }
}
