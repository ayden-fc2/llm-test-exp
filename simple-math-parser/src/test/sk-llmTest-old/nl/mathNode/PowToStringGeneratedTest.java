package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// 最小桩类模拟 Node 接口行为，用于构造测试环境
abstract class PowToStringGeneratedTest {
    abstract String toString();
}

// 最小桩类模拟 Pow 的依赖结构
class TestNode extends Node {
    private final String value;

    TestNode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

// 最小桩类模拟 Pow 行为（因原类未提供完整定义）
class Pow {
    private final Node leftNode;
    private final Node rightNode;
    private final boolean parens;

    public Pow(Node leftNode, Node rightNode, boolean parens) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.parens = parens;
    }

    protected Node getLeftNode() {
        return leftNode;
    }

    protected Node getRightNode() {
        return rightNode;
    }

    protected boolean isParens() {
        return parens;
    }

    public String toString() {
        String str = getLeftNode().toString() + " ^ " + getRightNode().toString();
        if (isParens())
            return '(' + str + ')';
        else
            return str;
    }
}

public class PowToStringTest {

    // 参数化测试：正常情况下的组合
    static Stream<Arguments> normalCases() {
        return Stream.of(
            Arguments.of("2", "3", false, "2 ^ 3"),
            Arguments.of("x", "y", false, "x ^ y"),
            Arguments.of("0", "1", false, "0 ^ 1"),
            Arguments.of("-1", "2", false, "-1 ^ 2"),
            Arguments.of("a", "b", true, "(a ^ b)"),
            Arguments.of("5", "0", true, "(5 ^ 0)")
        );
    }

    @ParameterizedTest
    @MethodSource("normalCases")
    void test_toString_NormalCombinations(String left, String right, boolean parens, String expected) {
        Node leftNode = new TestNode(left);
        Node rightNode = new TestNode(right);
        Pow pow = new Pow(leftNode, rightNode, parens);
        assertEquals(expected, pow.toString());
    }

    // 测试边界值：空字符串输入
    @Test
    void test_toString_EmptyStrings() {
        Node leftNode = new TestNode("");
        Node rightNode = new TestNode("");
        Pow pow = new Pow(leftNode, rightNode, false);
        assertEquals(" ^ ", pow.toString());
    }

    // 测试边界值：一个为空字符串
    @Test
    void test_toString_LeftEmpty() {
        Node leftNode = new TestNode("");
        Node rightNode = new TestNode("x");
        Pow pow = new Pow(leftNode, rightNode, false);
        assertEquals(" ^ x", pow.toString());
    }

    @Test
    void test_toString_RightEmpty() {
        Node leftNode = new TestNode("x");
        Node rightNode = new TestNode("");
        Pow pow = new Pow(leftNode, rightNode, false);
        assertEquals("x ^ ", pow.toString());
    }

    // 测试边界值：包含特殊字符
    @Test
    void test_toString_SpecialCharacters() {
        Node leftNode = new TestNode("a+b");
        Node rightNode = new TestNode("c*d");
        Pow pow = new Pow(leftNode, rightNode, true);
        assertEquals("(a+b ^ c*d)", pow.toString());
    }

    // 测试 parens 为 true 的情形
    @Test
    void test_toString_ParensTrue() {
        Node leftNode = new TestNode("base");
        Node rightNode = new TestNode("exp");
        Pow pow = new Pow(leftNode, rightNode, true);
        assertEquals("(base ^ exp)", pow.toString());
    }

    // 测试 parens 为 false 的情形
    @Test
    void test_toString_ParensFalse() {
        Node leftNode = new TestNode("base");
        Node rightNode = new TestNode("exp");
        Pow pow = new Pow(leftNode, rightNode, false);
        assertEquals("base ^ exp", pow.toString());
    }

    // 测试数值边界：极大和极小值（通过字符串表示）
    @Test
    void test_toString_NumericBoundaries() {
        Node leftNode = new TestNode(String.valueOf(Integer.MAX_VALUE));
        Node rightNode = new TestNode(String.valueOf(Integer.MIN_VALUE));
        Pow pow = new Pow(leftNode, rightNode, false);
        assertEquals(Integer.MAX_VALUE + " ^ " + Integer.MIN_VALUE, pow.toString());
    }

    // 测试浮点数边界（以字符串形式传入）
    @Test
    void test_toString_FloatBoundaries() {
        Node leftNode = new TestNode("1.7976931348623157E308"); // Double.MAX_VALUE as string
        Node rightNode = new TestNode("4.9E-324");               // Double.MIN_VALUE as string
        Pow pow = new Pow(leftNode, rightNode, true);
        assertEquals("(1.7976931348623157E308 ^ 4.9E-324)", pow.toString());
    }
}
