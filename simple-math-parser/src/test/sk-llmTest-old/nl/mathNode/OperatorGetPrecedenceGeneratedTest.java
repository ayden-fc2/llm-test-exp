package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Operator#getPrecedence()}.
 */
class OperatorGetPrecedenceGeneratedTest {

    // Minimal stub to allow compilation and testing of getPrecedence method
    static class Operator {
        private final int precedence;

        public Operator(int precedence) {
            this.precedence = precedence;
        }

        public int getPrecedence() {
            return precedence;
        }
    }

    @Test
    void test_getPrecedence_positiveValue() {
        Operator operator = new Operator(1);
        assertEquals(1, operator.getPrecedence());
    }

    @Test
    void test_getPrecedence_zeroValue() {
        Operator operator = new Operator(0);
        assertEquals(0, operator.getPrecedence());
    }

    @Test
    void test_getPrecedence_negativeValue() {
        Operator operator = new Operator(-1);
        assertEquals(-1, operator.getPrecedence());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, Integer.MAX_VALUE, 100, -100})
    void test_getPrecedence_boundaryAndExtremeValues(int precedence) {
        Operator operator = new Operator(precedence);
        assertEquals(precedence, operator.getPrecedence());
    }
}
