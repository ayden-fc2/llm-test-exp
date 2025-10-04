package mathTree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MathTreeInitGeneratedTest {

    // 正常表达式输入
    @ParameterizedTest
    @ValueSource(strings = {
        "3 + 4 * 2 / ( 1 - 5 )", 
        "sin(0)", 
        "cos(3.14159)", 
        "tan(0.785)", 
        "log(10)", 
        "sqrt(16)"
    })
    void test_Init_ValidExpression_ReturnsTrue(String validExpr) {
        MathTree tree = new MathTree();
        assertTrue(tree.init(validExpr), "初始化有效表达式应成功: " + validExpr);
    }

    // 异常或无效表达式输入
    @ParameterizedTest
    @ValueSource(strings = {
        "", 
        " ", 
        "()",
        "( 3 + 4", 
        "3 ++ 4", 
        "3 + / 4", 
        "unknownFunction(5)",
        "3 + ( 4 * 5"
    })
    void test_Init_InvalidExpression_ReturnsFalse(String invalidExpr) {
        MathTree tree = new MathTree();
        assertFalse(tree.init(invalidExpr), "初始化无效表达式应失败: " + invalidExpr);
    }

    // 特殊字符和边界情况
    @Test
    void test_Init_EmptyString_ReturnsFalse() {
        MathTree tree = new MathTree();
        assertFalse(tree.init(""), "空字符串初始化应失败");
    }

    @Test
    void test_Init_NullInput_ThrowsException() {
        MathTree tree = new MathTree();
        assertThrows(Exception.class, () -> tree.init(null), "null输入应当抛出异常");
    }

    @Test
    void test_Init_SingleNumber_ReturnsTrue() {
        MathTree tree = new MathTree();
        assertTrue(tree.init("42"), "单个数字应能成功初始化");
    }

    @Test
    void test_Init_VeryLargeNumber_ReturnsTrue() {
        MathTree tree = new MathTree();
        assertTrue(tree.init("1.7976931348623157E308"), "极大浮点数应能处理");
    }

    @Test
    void test_Init_Zero_ReturnsTrue() {
        MathTree tree = new MathTree();
        assertTrue(tree.init("0"), "零应该可以正确解析");
    }

    @Test
    void test_Init_NegativeNumber_ReturnsTrue() {
        MathTree tree = new MathTree();
        assertTrue(tree.init("-5"), "负数应该可以正确解析");
    }

    @Test
    void test_Init_WhitespaceOnly_ReturnsFalse() {
        MathTree tree = new MathTree();
        assertFalse(tree.init("   \t\n "), "纯空白字符串初始化应失败");
    }

    @Test
    void test_Init_ExpressionWithMismatchedParentheses_ReturnsFalse() {
        MathTree tree = new MathTree();
        assertFalse(tree.init("( 3 + 4 ) )"), "括号不匹配表达式应失败");
    }

    @Test
    void test_Init_ComplexValidExpression_ReturnsTrue() {
        MathTree tree = new MathTree();
        String complexExpr = "sin( sqrt(16) + cos(0) ) * log( e^2 )";
        assertTrue(tree.init(complexExpr), "复杂但有效的表达式应成功初始化");
    }

    @Test
    void test_Init_InvalidFunctionName_ReturnsFalse() {
        MathTree tree = new MathTree();
        assertFalse(tree.init("invalidFunc(5)"), "未知函数名称应导致初始化失败");
    }

    @Test
    void test_Init_MultipleOperatorsWithoutOperands_ReturnsFalse() {
        MathTree tree = new MathTree();
        assertFalse(tree.init("+ * -"), "仅有操作符没有操作数应失败");
    }

    @Test
    void test_Init_DivisionByZeroInStructure_ReturnsTrueButEvaluationMayFail() {
        MathTree tree = new MathTree();
        // 结构上合法，但求值时可能出错。这里只检查初始化是否成功
        assertTrue(tree.init("5 / ( 3 - 3 )"), "形式上合法但含潜在除零结构的表达式初始化应通过");
    }

    @Test
    void test_Init_ConsecutiveNumbersNoOperator_ReturnsFalse() {
        MathTree tree = new MathTree();
        assertFalse(tree.init("3 4"), "连续数字之间缺少运算符应失败");
    }

    @Test
    void test_Init_ExtremeFloatValues_ReturnsTrue() {
        MathTree tree = new MathTree();
        assertTrue(tree.init("3.4028235E38"), "接近Float.MAX_VALUE的值应能处理");
        assertTrue(tree.init("1.4E-45"), "接近Float.MIN_VALUE的值应能处理");
    }

    @Test
    void test_Init_IntegerMaxValue_ReturnsTrue() {
        MathTree tree = new MathTree();
        assertTrue(tree.init(String.valueOf(Integer.MAX_VALUE)), "整型最大值应能作为表达式元素");
    }

    @Test
    void test_Init_IntegerMinValue_ReturnsTrue() {
        MathTree tree = new MathTree();
        assertTrue(tree.init(String.valueOf(Integer.MIN_VALUE)), "整型最小值应能作为表达式元素");
    }

    @Test
    void test_Init_RepeatedInitOnSameInstance_DoesNotInterfere() {
        MathTree tree = new MathTree();

        assertTrue(tree.init("3 + 4"));
        
        // 第二次初始化不同的表达式
        assertTrue(tree.init("10 * 2"));

        // 再次初始化第一个表达式
        assertTrue(tree.init("3 + 4"));
    }
}
