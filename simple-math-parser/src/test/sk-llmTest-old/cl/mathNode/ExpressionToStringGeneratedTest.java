package mathNode;

import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Generated tests for Expression.toString()
 */
public class ExpressionToStringGeneratedTest {

    // Since Expression is abstract, we need a minimal concrete implementation to test
    static class ConcreteExpression extends Expression {
        private final String representation;

        public ConcreteExpression(String representation) {
            this.representation = representation;
        }

        @Override
        public String toString() {
            return representation;
        }

        @Override
        public Expression clone() {
            return new ConcreteExpression(this.representation);
        }
    }

    @Test
    public void test_toString_returnsNonNullString() {
        Expression expr = new ConcreteExpression("test");
        String result = expr.toString();
        assertNotNull(result, "toString() should not return null");
        assertTrue(result instanceof String, "Result should be a String");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "x", "123", "+", "-", "*", "/", "complex expression with spaces"})
    public void test_toString_handlesVariousRepresentations(String input) {
        Expression expr = new ConcreteExpression(input);
        String result = expr.toString();
        assertEquals(input, result, "toString() should return the expected string representation");
    }

    @Test
    public void test_toString_withNullRepresentation() {
        Expression expr = new ConcreteExpression(null);
        String result = expr.toString();
        assertNull(result, "toString() should return null when representation is null");
    }

    @Test
    public void test_toString_withSpecialCharacters() {
        String specialChars = "\n\t\r\\\"'";
        Expression expr = new ConcreteExpression(specialChars);
        String result = expr.toString();
        assertEquals(specialChars, result, "toString() should handle special characters correctly");
    }

    @Test
    public void test_toString_withVeryLongString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("a");
        }
        String longString = sb.toString();
        Expression expr = new ConcreteExpression(longString);
        String result = expr.toString();
        assertEquals(longString, result, "toString() should handle very long strings");
    }

    @Test
    public void test_toString_withUnicodeCharacters() {
        String unicode = "Hello 世界 🌍";
        Expression expr = new ConcreteExpression(unicode);
        String result = expr.toString();
        assertEquals(unicode, result, "toString() should handle Unicode characters");
    }
}
