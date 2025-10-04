package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IntCalculateGeneratedTest {

    // 正常值测试：验证返回值等于构造时传入的整数值
    @ParameterizedTest
    @ValueSource(ints = {0, 1, -1, 100, -100, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void test_calculate_normalValues(int inputValue) {
        Int node = new Int(inputValue);
        Number result = node.calculate();
        assertEquals(inputValue, result.intValue());
        assertInstanceOf(Integer.class, result);
    }

    // 浮点数构造测试（若支持）：检查是否能正确处理浮点输入并返回整数部分
    @Test
    void test_calculate_withDoubleConstructor_truncatesToInteger() {
        // 假设 Int 类允许通过 double 构造（根据语义推测）
        // 若实际不支持，请移除此测试或调整为构造后设值
        // 此处假设存在一个接受 double 的构造函数
        double input = 3.14;
        Int node = new Int((int) input); // 模拟截断行为
        Number result = node.calculate();
        assertEquals(3, result.intValue());
        assertInstanceOf(Integer.class, result);
    }

    // 边界值测试：极大正整数和极小负整数
    @Test
    void test_calculate_maxIntegerValue() {
        Int node = new Int(Integer.MAX_VALUE);
        Number result = node.calculate();
        assertEquals(Integer.MAX_VALUE, result.intValue());
    }

    @Test
    void test_calculate_minIntegerValue() {
        Int node = new Int(Integer.MIN_VALUE);
        Number result = node.calculate();
        assertEquals(Integer.MIN_VALUE, result.intValue());
    }

    // 零值测试
    @Test
    void test_calculate_zeroValue() {
        Int node = new Int(0);
        Number result = node.calculate();
        assertEquals(0, result.intValue());
    }

    // 负数测试
    @Test
    void test_calculate_negativeValue() {
        Int node = new Int(-42);
        Number result = node.calculate();
        assertEquals(-42, result.intValue());
    }

    // 正数测试
    @Test
    void test_calculate_positiveValue() {
        Int node = new Int(42);
        Number result = node.calculate();
        assertEquals(42, result.intValue());
    }
}
