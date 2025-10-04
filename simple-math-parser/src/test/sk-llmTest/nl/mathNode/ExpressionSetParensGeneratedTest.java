package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpressionSetParensGeneratedTest {

    @Test
    void test_setParens_true_setsParenthesisToTrue() {
        Expression expr = new Expression();
        expr.setParens(true);
        // Assuming there's a getParens() method for testing purposes.
        // If not, this test would require access to the 'parenthesis' field or an observable behavior.
        // For now, we'll assume such a getter exists for testability.
        assertTrue(expr.getParens());
    }

    @Test
    void test_setParens_false_setsParenthesisToFalse() {
        Expression expr = new Expression();
        expr.setParens(false);
        assertFalse(expr.getParens());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void test_setParens_idempotent_behavior(boolean value) {
        Expression expr = new Expression();
        expr.setParens(value);
        expr.setParens(value); // Set again
        if (value) {
            assertTrue(expr.getParens());
        } else {
            assertFalse(expr.getParens());
        }
    }

    // Note: Since boolean only has two values, boundary testing is implicitly covered above.
}
