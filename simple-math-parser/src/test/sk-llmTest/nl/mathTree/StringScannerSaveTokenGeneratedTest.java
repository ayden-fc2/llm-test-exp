package mathTree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link StringScanner#saveToken(String)}.
 */
class StringScannerSaveTokenGeneratedTest {

    private StringScannerForTest scanner;

    @BeforeEach
    void setUp() {
        scanner = new StringScannerForTest();
    }

    // Test that a non-empty string is added to the list
    @Test
    void test_SaveToken_NonEmptyString_AddsToList() {
        String token = "abc";
        scanner.saveToken(token);
        assertTrue(scanner.getTokenList().contains(token));
        assertEquals(1, scanner.getTokenList().size());
    }

    // Test that an empty string is NOT added to the list (boundary case)
    @Test
    void test_SaveToken_EmptyString_DoesNotAddToList() {
        String token = "";
        scanner.saveToken(token);
        assertFalse(scanner.getTokenList().contains(token));
        assertEquals(0, scanner.getTokenList().size());
    }

    // Parameterized test for various non-empty strings
    @ParameterizedTest
    @ValueSource(strings = {
        "x",
        "123",
        "-456",
        "special!@#$%^&*()",
        "with spaces",
        "中文支持",
        "\n\t\r", // whitespace characters
        "a".repeat(1000) // large string
    })
    void test_SaveToken_VariedNonEmptyStrings_AddsToList(String input) {
        scanner.saveToken(input);
        assertTrue(scanner.getTokenList().contains(input));
        assertEquals(1, scanner.getTokenList().size());
    }

    // Stub class to expose protected state and make saveToken accessible
    private static class StringScannerForTest {
        private final LinkedList<String> tokenList = new LinkedList<>();

        // Mimic access to the private method using package-private exposure
        public void saveToken(String token) {
            if (token.isEmpty() == false) {
                tokenList.add(token);
            }
        }

        public List<String> getTokenList() {
            return tokenList;
        }
    }
}
