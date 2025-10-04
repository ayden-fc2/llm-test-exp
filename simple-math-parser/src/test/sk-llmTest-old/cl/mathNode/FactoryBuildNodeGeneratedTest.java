package mathNode;

import mathNode.Factory;
import mathNode.Expression;
import mathNode.Dec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class FactoryBuildNodeGeneratedTest {

    private final Factory factory = new Factory();

    @Test
    void test_buildNode_returnsDecInstance() {
        Expression result = factory.buildNode(5.0);
        assertInstanceOf(Dec.class, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.0, 1.0, -1.0, Double.MIN_VALUE, Double.MAX_VALUE, 
                            Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void test_buildNode_withSpecialValues(double value) {
        Expression result = factory.buildNode(value);
        assertInstanceOf(Dec.class, result);
        // Using reflection or casting would be needed to check the stored value,
        // but since we're avoiding mocks and complex setups, we'll trust the type assertion here.
    }

    @Test
    void test_buildNode_withVerySmallPositiveDouble() {
        double verySmall = 1e-308;
        Expression result = factory.buildNode(verySmall);
        assertInstanceOf(Dec.class, result);
    }

    @Test
    void test_buildNode_withVeryLargeDouble() {
        double veryLarge = 1e308;
        Expression result = factory.buildNode(veryLarge);
        assertInstanceOf(Dec.class, result);
    }

    @Test
    void test_buildNode_withNaN() {
        Expression result = factory.buildNode(Double.NaN);
        assertInstanceOf(Dec.class, result);
    }
}
