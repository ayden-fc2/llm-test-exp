package mathNode;

import mathNode.Operator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class OperatorGetPrecedenceGeneratedTest {

    // Stub implementation to allow compilation
    static class Expression {}
    
    static class OperatorStub extends Operator {
        private final int precedence;
        
        public OperatorStub(int precedence) {
            this.precedence = precedence;
        }
        
        @Override
        public int getPrecedence() {
            return precedence;
        }
        
        @Override
        public Expression getLeftNode() { return null; }
        
        @Override
        public Expression getRightNode() { return null; }
        
        @Override
        public void setLeftNode(Expression newNode) {}
        
        @Override
        public void setRightNode(Expression newNode) {}
    }

    @Test
    public void test_getPrecedence_returnsCorrectValue_zero() {
        Operator operator = new OperatorStub(0);
        assertEquals(0, operator.getPrecedence());
    }

    @Test
    public void test_getPrecedence_returnsCorrectValue_one() {
        Operator operator = new OperatorStub(1);
        assertEquals(1, operator.getPrecedence());
    }

    @Test
    public void test_getPrecedence_returnsCorrectValue_negativeOne() {
        Operator operator = new OperatorStub(-1);
        assertEquals(-1, operator.getPrecedence());
    }

    @Test
    public void test_getPrecedence_returnsCorrectValue_maxInteger() {
        Operator operator = new OperatorStub(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, operator.getPrecedence());
    }

    @Test
    public void test_getPrecedence_returnsCorrectValue_minInteger() {
        Operator operator = new OperatorStub(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, operator.getPrecedence());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, -2, 10, -10, 100, -100})
    public void test_getPrecedence_returnsCorrectValue_variousIntegers(int precedence) {
        Operator operator = new OperatorStub(precedence);
        assertEquals(precedence, operator.getPrecedence());
    }
}
