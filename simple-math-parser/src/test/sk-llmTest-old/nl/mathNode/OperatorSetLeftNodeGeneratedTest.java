package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class OperatorSetLeftNodeGeneratedTest {

    // Minimal stub for Expression to satisfy compilation
    static class Expression {}

    @Test
    void test_setLeftNode_normal_case() {
        Operator operator = new Operator();
        Expression node = new Expression();
        operator.setLeftNode(node);
        assertEquals(node, operator.getLeftNode());
    }

    @Test
    void test_setLeftNode_with_null() {
        Operator operator = new Operator();
        operator.setLeftNode(null);
        assertNull(operator.getLeftNode());
    }

    @Test
    void test_setLeftNode_idempotent_behavior() {
        Operator operator = new Operator();
        Expression node1 = new Expression();
        Expression node2 = new Expression();

        operator.setLeftNode(node1);
        assertSame(node1, operator.getLeftNode());

        operator.setLeftNode(node2);
        assertSame(node2, operator.getLeftNode());
    }

    @Test
    void test_setLeftNode_multiple_calls_with_same_reference() {
        Operator operator = new Operator();
        Expression node = new Expression();

        operator.setLeftNode(node);
        operator.setLeftNode(node); // Set again
        assertSame(node, operator.getLeftNode());
    }
}
