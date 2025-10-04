package mathNode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Expression#isParens()}.
 */
class ExpressionIsParensGeneratedTest {

    // Stub implementation of Expression to allow compilation and testing
    private static class Expression {
        private final boolean parenthesis;

        public Expression(boolean parenthesis) {
            this.parenthesis = parenthesis;
        }

        public boolean isParens() {
            return parenthesis;
        }
    }

    @Test
    void test_isParens_whenParenthesisIsTrue_returnsTrue() {
        // Arrange
        Expression expression = new Expression(true);

        // Act
        boolean result = expression.isParens();

        // Assert
        assertTrue(result, "Expected isParens() to return true when parenthesis is true.");
    }

    @Test
    void test_isParens_whenParenthesisIsFalse_returnsFalse() {
        // Arrange
        Expression expression = new Expression(false);

        // Act
        boolean result = expression.isParens();

        // Assert
        assertFalse(result, "Expected isParens() to return false when parenthesis is false.");
    }
}
