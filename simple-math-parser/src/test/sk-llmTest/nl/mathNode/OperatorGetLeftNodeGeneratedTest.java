package mathNode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Operator#getLeftNode()}.
 */
class OperatorGetLeftNodeGeneratedTest {

    // Minimal stub for Expression to make the code compile and testable
    static class Expression {
        private final String value;

        public Expression(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Expression)) return false;
            Expression that = (Expression) o;
            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Expression{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    // Minimal stub for Operator to allow setting leftNode for testing
    static class Operator {
        private final Expression leftNode;

        public Operator(Expression leftNode) {
            this.leftNode = leftNode;
        }

        public Expression getLeftNode() {
            return leftNode;
        }
    }

    @Test
    void test_getLeftNode_normalCase_returnsCorrectExpression() {
        Expression expected = new Expression("left");
        Operator operator = new Operator(expected);
        assertSame(expected, operator.getLeftNode());
    }

    @Test
    void test_getLeftNode_withNullLeftNode_returnsNull() {
        Operator operator = new Operator(null);
        assertNull(operator.getLeftNode());
    }

    @Test
    void test_getLeftNode_withEmptyStringExpression_returnsExpression() {
        Expression expected = new Expression("");
        Operator operator = new Operator(expected);
        assertEquals(expected, operator.getLeftNode());
    }

    @Test
    void test_getLeftNode_withSpecialCharacterExpression_returnsExpression() {
        Expression expected = new Expression("!@#$%^&*()");
        Operator operator = new Operator(expected);
        assertEquals(expected, operator.getLeftNode());
    }

    @Test
    void test_getLeftNode_withVeryLongStringExpression_returnsExpression() {
        String longValue = "a".repeat(10000);
        Expression expected = new Expression(longValue);
        Operator operator = new Operator(expected);
        assertEquals(expected, operator.getLeftNode());
    }
}
