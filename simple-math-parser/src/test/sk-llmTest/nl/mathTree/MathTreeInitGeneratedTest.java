package mathTree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MathTreeInitGeneratedTest {

    // 正常输入示例
    @Test
    void test_init_validExpression_returnsTrue() {
        MathTree mathTree = new MathTree();
        assertTrue(mathTree.init("3 + 4 * 2"));
    }

    @Test
    void test_init_complexValidExpression_returnsTrue() {
        MathTree mathTree = new MathTree();
        assertTrue(mathTree.init("(1 + 2) * (3 - 4 / 5)"));
    }

    // 异常/非法输入示例
    @Test
    void test_init_invalidExpression_returnsFalse() {
        MathTree mathTree = new MathTree();
        assertFalse(mathTree.init("3 + * 4"));
    }

    @Test
    void test_init_emptyString_returnsFalse() {
        MathTree mathTree = new MathTree();
        assertFalse(mathTree.init(""));
    }

    @Test
    void test_init_nullInput_returnsFalse() {
        MathTree mathTree = new MathTree();
        assertFalse(mathTree.init(null));
    }

    // 边界值与特殊取值
    @ParameterizedTest
    @ValueSource(strings = {
        "0",
        "1",
        "-1",
        "2147483647",  // Integer.MAX_VALUE
        "-2147483648"  // Integer.MIN_VALUE
    })
    void test_init_boundaryIntegerValues_returnsTrue(String expr) {
        MathTree mathTree = new MathTree();
        assertTrue(mathTree.init(expr));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "1.0",
        "0.0",
        "-1.0",
        "1.7976931348623157E308",  // Double.MAX_VALUE
        "4.9E-324"                 // Double.MIN_VALUE
    })
    void test_init_boundaryDoubleValues_returnsTrue(String expr) {
        MathTree mathTree = new MathTree();
        assertTrue(mathTree.init(expr));
    }

    // 特殊情况：检查树失败的情况（需要构造特定场景）
    @Test
    void test_init_checkTreeFails_setsRootToNullAndReturnsFalse() {
        MathTree mathTree = new MathTree();
        // 假设一个不完整的表达式会导致 buildTree 成功但 checkTree 失败
        // 注意：这依赖于内部实现，我们假设存在这样的输入
        // 这里使用一个可能触发该路径的示例（具体取决于 cleanStrList 和 buildTree 实现）
        boolean result = mathTree.init("1 + ");
        assertFalse(result);
        // 验证 rootNode 被置为 null（如果可以访问）
        // 由于无法直接访问 rootNode，我们只能通过返回值判断
    }

    // 空白字符处理
    @Test
    void test_init_whitespaceOnly_returnsFalse() {
        MathTree mathTree = new MathTree();
        assertFalse(mathTree.init("   \t\n  "));
    }

    @Test
    void test_init_leadingAndTrailingSpaces_returnsTrue() {
        MathTree mathTree = new MathTree();
        assertTrue(mathTree.init("  3 + 4  "));
    }

    // 极大输入测试（压力测试）
    @Test
    void test_init_veryLargeExpression_returnsTrue() {
        MathTree mathTree = new MathTree();
        String largeExpr = "1" + " + 1".repeat(1000); // 构造一个很长的表达式
        assertTrue(mathTree.init(largeExpr));
    }

    // 测试 rootNode 在失败后被置为 null 的行为（间接测试）
    @Test
    void test_init_multipleCalls_invalidThenValid_returnsFalseThenTrue() {
        MathTree mathTree = new MathTree();
        
        // 第一次调用无效表达式
        boolean firstResult = mathTree.init("invalid");
        assertFalse(firstResult);
        
        // 第二次调用有效表达式
        boolean secondResult = mathTree.init("1 + 1");
        assertTrue(secondResult);
    }
}
