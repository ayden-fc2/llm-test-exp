package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Factory#buildNode(double)}.
 */
class FactoryBuildNodeGeneratedTest {

    private final Factory factory = new Factory();

    // Normal cases
    @Test
    void test_BuildNode_WithPositiveDouble_ReturnsDecWithSameValue() {
        double input = 3.14;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    @Test
    void test_BuildNode_WithNegativeDouble_ReturnsDecWithSameValue() {
        double input = -2.5;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    @Test
    void test_BuildNode_WithZero_ReturnsDecWithZero() {
        double input = 0.0;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    // Boundary and special values
    @ParameterizedTest
    @ValueSource(doubles = {Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_NORMAL, 
                            Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void test_BuildNode_WithExtremeValues_ReturnsDecWithSameValue(double value) {
        Expression result = factory.buildNode(value);
        assertInstanceOf(Dec.class, result);
        assertEquals(value, ((Dec) result).getValue());
    }

    @Test
    void test_BuildNode_WithNaN_ThrowsNoExceptionAndReturnsDecWithNaN() {
        double input = Double.NaN;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertTrue(Double.isNaN(((Dec) result).getValue()));
    }

    // Small and large magnitude numbers
    @Test
    void test_BuildNode_WithVerySmallPositiveDouble_ReturnsDecWithSameValue() {
        double input = 1e-300;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-309);
    }

    @Test
    void test_BuildNode_WithVeryLargeNegativeDouble_ReturnsDecWithSameValue() {
        double input = -1e300;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-309);
    }
}
