package mathTree;

import mathTree.StringScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringScannerSaveTokenGeneratedTest {

    private StringScanner scanner;
    private Field tokenListField;

    @BeforeEach
    void setUp() throws Exception {
        scanner = new StringScanner();
        tokenListField = StringScanner.class.getDeclaredField("tokenList");
        tokenListField.setAccessible(true);
    }

    @Test
    void test_saveToken_withNonEmptyString_addsTokenToList() throws Exception {
        Method saveTokenMethod = StringScanner.class.getDeclaredMethod("saveToken", String.class);
        saveTokenMethod.setAccessible(true);

        saveTokenMethod.invoke(scanner, "test");

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertEquals(1, tokenList.size());
        assertEquals("test", tokenList.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
    void test_saveToken_withEmptyOrWhitespaceStrings_doesNotAddToTokenList(String input) throws Exception {
        Method saveTokenMethod = StringScanner.class.getDeclaredMethod("saveToken", String.class);
        saveTokenMethod.setAccessible(true);

        saveTokenMethod.invoke(scanner, input);

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertTrue(tokenList.isEmpty());
    }

    @Test
    void test_saveToken_withMultipleTokens_addsAllTokens() throws Exception {
        Method saveTokenMethod = StringScanner.class.getDeclaredMethod("saveToken", String.class);
        saveTokenMethod.setAccessible(true);

        saveTokenMethod.invoke(scanner, "first");
        saveTokenMethod.invoke(scanner, "second");
        saveTokenMethod.invoke(scanner, "");

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertEquals(2, tokenList.size());
        assertEquals("first", tokenList.get(0));
        assertEquals("second", tokenList.get(1));
    }

    @Test
    void test_saveToken_withSpecialCharacters_addsTokenToList() throws Exception {
        Method saveTokenMethod = StringScanner.class.getDeclaredMethod("saveToken", String.class);
        saveTokenMethod.setAccessible(true);

        String special = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        saveTokenMethod.invoke(scanner, special);

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertEquals(1, tokenList.size());
        assertEquals(special, tokenList.get(0));
    }

    @Test
    void test_saveToken_withVeryLongString_addsTokenToList() throws Exception {
        Method saveTokenMethod = StringScanner.class.getDeclaredMethod("saveToken", String.class);
        saveTokenMethod.setAccessible(true);

        String longString = "a".repeat(10000);
        saveTokenMethod.invoke(scanner, longString);

        @SuppressWarnings("unchecked")
        List<String> tokenList = (List<String>) tokenListField.get(scanner);
        assertEquals(1, tokenList.size());
        assertEquals(longString, tokenList.get(0));
    }

    @Test
    void test_saveToken_withNullString_throwsException() throws Exception {
        Method saveTokenMethod = StringScanner.class.getDeclaredMethod("saveToken", String.class);
        saveTokenMethod.setAccessible(true);

        assertThrows(NullPointerException.class, () -> {
            saveTokenMethod.invoke(scanner, (String) null);
        });
    }
}
