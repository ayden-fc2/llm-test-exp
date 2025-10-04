package mathNode;

import mathNode.Int;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class IntToStringGeneratedTest {

    // Test toString with positive values without parentheses
    @ParameterizedTest
    @ValueSource(ints = {1, 42, 1000})
    public void test_toString_positiveValueWithoutParens_returnsPlainString(int value) {
        Int node = new Int(value);
        node.setParens(false);
        assertEquals(Integer.toString(value), node.toString());
    }

    // Test toString with zero without parentheses
    @Test
    public void test_toString_zeroWithoutParens_returnsZeroString() {
        Int node = new Int(0);
        node.setParens(false);
        assertEquals("0", node.toString());
    }

    // Test toString with negative values without parentheses
    @ParameterizedTest
    @ValueSource(ints = {-1, -42, -1000})
    public void test_toString_negativeValueWithoutParens_returnsPlainString(int value) {
        Int node = new Int(value);
        node.setParens(false);
        assertEquals(Integer.toString(value), node.toString());
    }

    // Test toString with positive values with parentheses
    @ParameterizedTest
    @ValueSource(ints = {1, 42, 1000})
    public void test_toString_positiveValueWithParens_returnsParenthesizedString(int value) {
        Int node = new Int(value);
        node.setParens(true);
        assertEquals("(" + value + ")", node.toString());
    }

    // Test toString with zero with parentheses
    @Test
    public void test_toString_zeroWithParens_returnsParenthesizedZeroString() {
        Int node = new Int(0);
        node.setParens(true);
        assertEquals("(0)", node.toString());
    }

    // Test toString with negative values with parentheses
    @ParameterizedTest
    @ValueSource(ints = {-1, -42, -1000})
    public void test_toString_negativeValueWithParens_returnsParenthesizedString(int value) {
        Int node = new Int(value);
        node.setParens(true);
        assertEquals("(" + value + ")", node.toString());
    }

    // Test toString with Integer.MAX_VALUE without parentheses
    @Test
    public void test_toString_maxIntWithoutParens_returnsMaxIntString() {
        Int node = new Int(Integer.MAX_VALUE);
        node.setParens(false);
        assertEquals(Integer.toString(Integer.MAX_VALUE), node.toString());
    }

    // Test toString with Integer.MIN_VALUE without parentheses
    @Test
    public void test_toString_minIntWithoutParens_returnsMinIntString() {
        Int node = new Int(Integer.MIN_VALUE);
        node.setParens(false);
        assertEquals(Integer.toString(Integer.MIN_VALUE), node.toString());
    }

    // Test toString with Integer.MAX_VALUE with parentheses
    @Test
    public void test_toString_maxIntWithParens_returnsParenthesizedMaxIntString() {
        Int node = new Int(Integer.MAX_VALUE);
        node.setParens(true);
        assertEquals("(" + Integer.MAX_VALUE + ")", node.toString());
    }

    // Test toString with Integer.MIN_VALUE with parentheses
    @Test
    public void test_toString_minIntWithParens_returnsParenthesizedMinIntString() {
        Int node = new Int(Integer.MIN_VALUE);
        node.setParens(true);
        assertEquals("(" + Integer.MIN_VALUE + ")", node.toString());
    }
}
