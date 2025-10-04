package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IntCalculateGeneratedTest {

    // 正常值测试：验证返回值等于构造时传入的整数值
    @ParameterizedTest
    @ValueSource(ints = {0, 1, -1, 42, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void test_calculate_normalValues(int inputValue) {
        Int intNode = new Int(inputValue);
        Number result = intNode.calculate();
        assertEquals(inputValue, result.intValue(), "Returned value should match the input");
        assertTrue(result instanceof Integer, "Result should be an instance of Integer");
    }

    // 特殊边界值测试：确保极值也能正确返回
    @Test
    void test_calculate_maxInteger() {
        Int intNode = new Int(Integer.MAX_VALUE);
        Number result = intNode.calculate();
        assertEquals(Integer.MAX_VALUE, result.intValue());
    }

    @Test
    void test_calculate_minInteger() {
        Int intNode = new Int(Integer.MIN_VALUE);
        Number result = intNode.calculate();
        assertEquals(Integer.MIN_VALUE, result.intValue());
    }

    // 零值测试
    @Test
    void test_calculate_zero() {
        Int intNode = new Int(0);
        Number result = intNode.calculate();
        assertEquals(0, result.intValue());
    }

    // 负数测试
    @Test
    void test_calculate_negativeOne() {
        Int intNode = new Int(-1);
        Number result = intNode.calculate();
        assertEquals(-1, result.intValue());
    }

    // 正数测试
    @Test
    void test_calculate_positiveOne() {
        Int intNode = new Int(1);
        Number result = intNode.calculate();
        assertEquals(1, result.intValue());
    }
}
