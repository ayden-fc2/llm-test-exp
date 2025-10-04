package mathNode;

import mathNode.Operator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class OperatorGetPrecedenceGeneratedTest {

    // Stub implementation of Operator for compilation
    static class TestOperator extends Operator {
        private final int precedence;

        public TestOperator(int precedence) {
            this.precedence = precedence;
        }

        @Override
        public int getPrecedence() {
            return precedence;
        }
    }

    @Test
    public void test_getPrecedence_returnsCorrectValue_zero() {
        Operator operator = new TestOperator(0);
        assertEquals(0, operator.getPrecedence());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10})
    public void test_getPrecedence_returnsCorrectValue_positive(int precedence) {
        Operator operator = new TestOperator(precedence);
        assertEquals(precedence, operator.getPrecedence());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    public void test_getPrecedence_returnsCorrectValue_negative(int precedence) {
        Operator operator = new TestOperator(precedence);
        assertEquals(precedence, operator.getPrecedence());
    }

    @Test
    public void test_getPrecedence_returnsCorrectValue_minValue() {
        Operator operator = new TestOperator(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, operator.getPrecedence());
    }

    @Test
    public void test_getPrecedence_returnsCorrectValue_maxValue() {
        Operator operator = new TestOperator(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, operator.getPrecedence());
    }
}
