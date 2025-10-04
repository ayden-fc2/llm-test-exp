package mathNode;

import mathNode.Operator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class OperatorSetParensGeneratedTest {

    @Test
    public void test_setParens_true_setsParenthesisAndPrecedence() {
        Operator operator = new Operator();
        operator.setParens(true);
        assertTrue(operator.parenthesis);
        assertEquals(0, operator.precedence);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void test_setParens_variousValues_setsParenthesisOnly(boolean value) {
        Operator operator = new Operator();
        operator.setParens(value);
        assertEquals(value, operator.parenthesis);
        if (!value) {
            assertNotEquals(0, operator.precedence);
        }
    }

    @Test
    public void test_setParens_false_doesNotChangePrecedence() {
        Operator operator = new Operator();
        int initialPrecedence = operator.precedence;
        operator.setParens(false);
        assertEquals(initialPrecedence, operator.precedence);
    }
}
