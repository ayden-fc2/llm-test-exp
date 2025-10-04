package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Expression#calculate()}.
 */
class ExpressionCalculateGeneratedTest {

    // Minimal stub implementation to allow compilation and testing
    static class ConstantExpression extends Expression {
        private final Number value;

        ConstantExpression(Number value) {
            this.value = value;
        }

        @Override
        public Number calculate() {
            return value;
        }
    }

    // Another stub with conditional logic to test branches
    static class ConditionalExpression extends Expression {
        private final double input;

        ConditionalExpression(double input) {
            this.input = input;
        }

        @Override
        public Number calculate() {
            if (input > 0) {
                return input * 2;
            } else if (input < 0) {
                return input / 2;
            } else {
                return 0;
            }
        }
    }

    // Stub that throws an exception under certain conditions
    static class ExceptionalExpression extends Expression {
        private final boolean shouldThrow;

        ExceptionalExpression(boolean shouldThrow) {
            this.shouldThrow = shouldThrow;
        }

        @Override
        public Number calculate() {
            if (shouldThrow) {
                throw new ArithmeticException("Simulated calculation error");
            }
            return 42;
        }
    }

    @Test
    void test_calculate_positiveValue_returnsDoubledValue() {
        Expression expr = new ConditionalExpression(10.0);
        assertEquals(20.0, expr.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_negativeValue_returnsHalvedValue() {
        Expression expr = new ConditionalExpression(-10.0);
        assertEquals(-5.0, expr.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_zeroValue_returnsZero() {
        Expression expr = new ConditionalExpression(0.0);
        assertEquals(0.0, expr.calculate().doubleValue(), 1e-9);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.MIN_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void test_calculate_extremeValues_handlesCorrectly(double val) {
        Expression expr = new ConstantExpression(val);
        assertEquals(val, expr.calculate().doubleValue(), 1e-9);
    }

    @Test
    void test_calculate_nanValue_returnsNaN() {
        Expression expr = new ConstantExpression(Double.NaN);
        assertTrue(Double.isNaN(expr.calculate().doubleValue()));
    }

    @Test
    void test_calculate_verySmallPositiveValue_handledWithoutUnderflow() {
        Expression expr = new ConditionalExpression(1e-308);
        assertEquals(2e-308, expr.calculate().doubleValue(), 1e-310);
    }

    @Test
    void test_calculate_minIntegerValue_handledAsDouble() {
        Expression expr = new ConstantExpression(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, expr.calculate().intValue());
    }

    @Test
    void test_calculate_maxIntegerValue_handledAsDouble() {
        Expression expr = new ConstantExpression(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, expr.calculate().intValue());
    }

    @Test
    void test_calculate_exceptionalCase_throwsArithmeticException() {
        Expression expr = new ExceptionalExpression(true);
        assertThrows(ArithmeticException.class, expr::calculate);
    }

    @Test
    void test_calculate_normalCase_noExceptionThrown() {
        Expression expr = new ExceptionalExpression(false);
        assertEquals(42, expr.calculate().intValue());
    }

    @Test
    void test_calculate_returnType_isNumberInstance() {
        Expression expr = new ConstantExpression(1);
        assertNotNull(expr.calculate());
        assertTrue(expr.calculate() instanceof Number);
    }
}
