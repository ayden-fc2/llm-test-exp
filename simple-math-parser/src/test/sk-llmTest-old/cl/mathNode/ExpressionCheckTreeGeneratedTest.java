package mathNode;

import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressionCheckTreeGeneratedTest {

    // Since Expression is abstract, we need a concrete implementation for testing
    static class ConcreteExpression extends Expression {
        private final boolean returnValue;
        
        public ConcreteExpression(boolean returnValue) {
            this.returnValue = returnValue;
        }
        
        @Override
        public boolean checkTree() {
            return returnValue;
        }
    }

    @Test
    @DisplayName("test_checkTree_returnsTrue_whenConditionIsTrue")
    public void test_checkTree_returnsTrue_whenConditionIsTrue() {
        Expression expr = new ConcreteExpression(true);
        assertTrue(expr.checkTree(), "checkTree should return true when configured to do so");
    }

    @Test
    @DisplayName("test_checkTree_returnsFalse_whenConditionIsFalse")
    public void test_checkTree_returnsFalse_whenConditionIsFalse() {
        Expression expr = new ConcreteExpression(false);
        assertFalse(expr.checkTree(), "checkTree should return false when configured to do so");
    }

    @Test
    @DisplayName("test_checkTree_isDeterministic_whenCalledMultipleTimes")
    public void test_checkTree_isDeterministic_whenCalledMultipleTimes() {
        Expression expr = new ConcreteExpression(true);
        
        boolean firstResult = expr.checkTree();
        boolean secondResult = expr.checkTree();
        boolean thirdResult = expr.checkTree();
        
        assertEquals(firstResult, secondResult, "checkTree should return consistent results");
        assertEquals(secondResult, thirdResult, "checkTree should return consistent results");
        assertTrue(firstResult, "All calls should return true");
    }

    @Test
    @DisplayName("test_checkTree_withFalseValue_isDeterministic")
    public void test_checkTree_withFalseValue_isDeterministic() {
        Expression expr = new ConcreteExpression(false);
        
        boolean firstResult = expr.checkTree();
        boolean secondResult = expr.checkTree();
        boolean thirdResult = expr.checkTree();
        
        assertEquals(firstResult, secondResult, "checkTree should return consistent results");
        assertEquals(secondResult, thirdResult, "checkTree should return consistent results");
        assertFalse(firstResult, "All calls should return false");
    }
}
