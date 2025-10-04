package mathTree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 单元测试类用于测试 StringScanner.isDelim(char) 方法。
 * 由于 isDelim 是私有方法，此处假设通过反射或包级访问进行测试。
 * 若无法直接访问，需提供公共包装方法或调整访问修饰符。
 */
class StringScannerIsDelimGeneratedTest {

    private StringScanner scannerWithWhitespaceEnabled;
    private StringScanner scannerWithWhitespaceDisabled;

    @BeforeEach
    void setUp() {
        // 初始化两个实例，分别测试 skipWhitespace 为 true 和 false 的情况
        scannerWithWhitespaceEnabled = new StringScanner("") {
            {
                skipWhitespace = true;
                delimSet = new HashSet<>();
                delimSet.add(',');
                delimSet.add(';');
            }
        };

        scannerWithWhitespaceDisabled = new StringScanner("") {
            {
                skipWhitespace = false;
                delimSet = new HashSet<>();
                delimSet.add(',');
                delimSet.add(';');
            }
        };
    }

    // --- 分支覆盖测试 ---

    @Test
    void test_isDelim_returnsTrue_whenSkipWhitespaceTrue_andCharIsWhitespace() throws Exception {
        assertTrue((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceEnabled, ' '));
        assertTrue((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceEnabled, '\t'));
        assertTrue((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceEnabled, '\n'));
    }

    @Test
    void test_isDelim_returnsTrue_whenCharIsInDelimSet() throws Exception {
        assertTrue((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceEnabled, ','));
        assertTrue((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceDisabled, ';'));
    }

    @Test
    void test_isDelim_returnsFalse_whenSkipWhitespaceFalse_andCharIsWhitespace() throws Exception {
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceDisabled, ' '));
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceDisabled, '\t'));
    }

    @Test
    void test_isDelim_returnsFalse_whenCharIsNotWhitespace_andNotInDelimSet() throws Exception {
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceEnabled, 'a'));
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceDisabled, '1'));
    }

    // --- 边界值与特殊值测试 ---

    @ParameterizedTest
    @ValueSource(chars = { 0, 1, -1, Character.MIN_VALUE, Character.MAX_VALUE })
    void test_isDelim_withBoundaryCharValues(char ch) throws Exception {
        // 这些字符既不是空白也不是分隔符，应返回 false
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceDisabled, ch));
        // 如果是空白字符，则在 skipWhitespace=true 时返回 true
        boolean isWhitespace = Character.isWhitespace(ch);
        assertEquals(isWhitespace, (Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceEnabled, ch));
    }

    @Test
    void test_isDelim_withUnicodeWhitespace() throws Exception {
        // 测试其他 Unicode 空白字符
        assertTrue((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceEnabled, '\u2000')); // EN QUAD
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceDisabled, '\u2000'));
    }

    @Test
    void test_isDelim_withNonBmpUnicodeChar() throws Exception {
        // 测试非基本多文种平面字符 (Non-BMP)
        char highSurrogate = '\uD83D'; // 代理对的一部分
        char lowSurrogate = '\uDE00';  // 代理对的一部分
        // 单独的代理项不是空白也不是分隔符
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceDisabled, highSurrogate));
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceDisabled, lowSurrogate));
        // skipWhitespace 对代理项无效，因为 Character.isWhitespace 不认为它们是空白
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceEnabled, highSurrogate));
        assertFalse((Boolean) getIsDelimMethod().invoke(scannerWithWhitespaceEnabled, lowSurrogate));
    }

    // --- 辅助方法 ---

    private java.lang.reflect.Method getIsDelimMethod() throws NoSuchMethodException {
        java.lang.reflect.Method method = StringScanner.class.getDeclaredMethod("isDelim", char.class);
        method.setAccessible(true);
        return method;
    }
}
