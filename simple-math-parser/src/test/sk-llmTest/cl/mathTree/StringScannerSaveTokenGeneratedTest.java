package mathTree;

import mathTree.StringScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StringScannerSaveTokenGeneratedTest {

    private StringScanner scanner;
    private Method saveTokenMethod;
    private Field tokenListField;

    @BeforeEach
    public void setUp() throws Exception {
        scanner = new StringScanner();
        saveTokenMethod = StringScanner.class.getDeclaredMethod("saveToken", String.class);
        saveTokenMethod.setAccessible(true);
        tokenListField = StringScanner.class.getDeclaredField("tokenList");
        tokenListField.setAccessible(true);
    }

    @Test
    public void test_saveToken_withNonEmptyString_addsToTokenList() throws Exception {
        String token = "test";
        saveTokenMethod.invoke(scanner, token);

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertEquals(1, tokenList.size());
        assertEquals(token, tokenList.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    public void test_saveToken_withEmptyOrWhitespace_doesNotAddToTokenList(String token) throws Exception {
        saveTokenMethod.invoke(scanner, token);

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertTrue(tokenList.isEmpty());
    }

    @Test
    public void test_saveToken_withNull_throwsNullPointerException() throws Exception {
        assertThrows(InvocationTargetException.class, () -> {
            saveTokenMethod.invoke(scanner, new Object[]{null});
        });
    }

    @Test
    public void test_saveToken_multipleCalls_accumulatesTokens() throws Exception {
        saveTokenMethod.invoke(scanner, "first");
        saveTokenMethod.invoke(scanner, "");
        saveTokenMethod.invoke(scanner, "second");

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertEquals(2, tokenList.size());
        assertEquals("first", tokenList.get(0));
        assertEquals("second", tokenList.get(1));
    }

    @Test
    public void test_saveToken_withSpecialCharacters_storesCorrectly() throws Exception {
        String specialToken = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        saveTokenMethod.invoke(scanner, specialToken);

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertEquals(1, tokenList.size());
        assertEquals(specialToken, tokenList.get(0));
    }

    @Test
    public void test_saveToken_withVeryLongString_storesCorrectly() throws Exception {
        String longToken = "a".repeat(10000);
        saveTokenMethod.invoke(scanner, longToken);

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertEquals(1, tokenList.size());
        assertEquals(longToken, tokenList.get(0));
    }

    @Test
    public void test_saveToken_withUnicodeCharacters_storesCorrectly() throws Exception {
        String unicodeToken = "こんにちは世界🌍";
        saveTokenMethod.invoke(scanner, unicodeToken);

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertEquals(1, tokenList.size());
        assertEquals(unicodeToken, tokenList.get(0));
    }
}
