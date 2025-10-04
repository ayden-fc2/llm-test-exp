package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FactoryBuildNodeGeneratedTest {

    private final Factory factory = new Factory();

    // 正常值测试：代表性小数
    @Test
    void test_BuildNode_WithPositiveDecimal_ReturnsDecInstance() {
        double input = 3.14;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    // 正常值测试：负数
    @Test
    void test_BuildNode_WithNegativeValue_ReturnsDecInstance() {
        double input = -2.5;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    // 边界值测试：零
    @Test
    void test_BuildNode_WithZero_ReturnsDecInstanceWithZero() {
        double input = 0.0;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    // 边界值测试：1
    @Test
    void test_BuildNode_WithOne_ReturnsDecInstanceWithOne() {
        double input = 1.0;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    // 边界值测试：-1
    @Test
    void test_BuildNode_WithNegativeOne_ReturnsDecInstanceWithNegativeOne() {
        double input = -1.0;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    // 极值测试：Double.MAX_VALUE
    @Test
    void test_BuildNode_WithMaxDouble_ReturnsDecInstance() {
        double input = Double.MAX_VALUE;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    // 极值测试：Double.MIN_VALUE（最小正数）
    @Test
    void test_BuildNode_WithMinPositiveDouble_ReturnsDecInstance() {
        double input = Double.MIN_VALUE;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 1e-9);
    }

    // 极值测试：非常小的数（接近于零但非零）
    @ParameterizedTest
    @ValueSource(doubles = {1e-308, -1e-308})
    void test_BuildNode_WithVerySmallValues_ReturnsDecInstance(double input) {
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), Math.abs(input) * 1e-9);
    }

    // 极值测试：非常大的数
    @ParameterizedTest
    @ValueSource(doubles = {1e307, -1e307})
    void test_BuildNode_WithVeryLargeValues_ReturnsDecInstance(double input) {
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), Math.abs(input) * 1e-9);
    }

    // 特殊值测试：正无穷
    @Test
    void test_BuildNode_WithPositiveInfinity_ReturnsDecInstance() {
        double input = Double.POSITIVE_INFINITY;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 0.0);
    }

    // 特殊值测试：负无穷
    @Test
    void test_BuildNode_WithNegativeInfinity_ReturnsDecInstance() {
        double input = Double.NEGATIVE_INFINITY;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertEquals(input, ((Dec) result).getValue(), 0.0);
    }

    // 特殊值测试：NaN
    @Test
    void test_BuildNode_WithNaN_ReturnsDecInstance() {
        double input = Double.NaN;
        Expression result = factory.buildNode(input);
        assertInstanceOf(Dec.class, result);
        assertTrue(Double.isNaN(((Dec) result).getValue()));
    }
}
