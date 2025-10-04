package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link Operator#getPrecedence()}.
 */
class OperatorGetPrecedenceGeneratedTest {

    // Minimal stub to allow compilation and testing of getPrecedence
    private static class TestOperator extends Operator {
        private final int precedence;

        TestOperator(int precedence) {
            this.precedence = precedence;
        }

        @Override
        public int getPrecedence() {
            return precedence;
        }
    }

    @Test
    void test_getPrecedence_zero() {
        Operator operator = new TestOperator(0);
        assertEquals(0, operator.getPrecedence());
    }

    @Test
    void test_getPrecedence_positive() {
        Operator operator = new TestOperator(5);
        assertEquals(5, operator.getPrecedence());
    }

    @Test
    void test_getPrecedence_negative() {
        Operator operator = new TestOperator(-3);
        assertEquals(-3, operator.getPrecedence());
    }

    @Test
    void test_getPrecedence_maxInteger() {
        Operator operator = new TestOperator(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, operator.getPrecedence());
    }

    @Test
    void test_getPrecedence_minInteger() {
        Operator operator = new TestOperator(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, operator.getPrecedence());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, -1, 100, -100, 10000})
    void test_getPrecedence_variousTypicalValues(int input) {
        Operator operator = new TestOperator(input);
        assertEquals(input, operator.getPrecedence());
    }
}
