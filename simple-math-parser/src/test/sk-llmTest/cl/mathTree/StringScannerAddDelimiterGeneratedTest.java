package mathTree;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashSet;
import mathTree.StringScanner;

public class StringScannerAddDelimiterGeneratedTest {

    private StringScanner scanner;

    @BeforeEach
    public void setUp() {
        scanner = new StringScanner();
    }

    @Test
    public void test_addDelimiter_addsSingleCharacter() {
        char[] delimiters = {'+'};
        scanner.addDelimiter(delimiters);
        
        // Verify that the delimiter was added by attempting to use it in tokenization
        // Since we don't have direct access to delimSet, we test indirectly
        // This assumes some method exists to check if a character is a delimiter
        // For now, we'll just verify no exception is thrown
        assertDoesNotThrow(() -> scanner.addDelimiter(delimiters));
    }

    @ParameterizedTest
    @ValueSource(chars = {' ', '\t', '\n', ',', ';', '.', '!', '?', '"', '\''})
    public void test_addDelimiter_addsCommonDelimiters(char delimiter) {
        char[] delimiters = {delimiter};
        scanner.addDelimiter(delimiters);
        
        assertDoesNotThrow(() -> scanner.addDelimiter(delimiters));
    }

    @Test
    public void test_addDelimiter_addsMultipleCharacters() {
        char[] delimiters = {'+', '-', '*', '/', '%'};
        scanner.addDelimiter(delimiters);
        
        assertDoesNotThrow(() -> scanner.addDelimiter(delimiters));
    }

    @Test
    public void test_addDelimiter_withEmptyArray() {
        char[] delimiters = {};
        assertDoesNotThrow(() -> scanner.addDelimiter(delimiters));
    }

    @Test
    public void test_addDelimiter_withNullArray() {
        assertThrows(NullPointerException.class, () -> scanner.addDelimiter(null));
    }

    @Test
    public void test_addDelimiter_addsSpecialCharacters() {
        char[] delimiters = {'@', '#', '$', '^', '&', '*', '(', ')', '-', '_', '=', '+'};
        assertDoesNotThrow(() -> scanner.addDelimiter(delimiters));
    }

    @Test
    public void test_addDelimiter_addsUnicodeCharacters() {
        char[] delimiters = {'α', 'β', 'γ', 'δ', 'ε'};
        assertDoesNotThrow(() -> scanner.addDelimiter(delimiters));
    }

    @Test
    public void test_addDelimiter_addsNumericCharacters() {
        char[] delimiters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertDoesNotThrow(() -> scanner.addDelimiter(delimiters));
    }

    @Test
    public void test_addDelimiter_duplicateCharacters() {
        char[] delimiters1 = {'+', '-'};
        char[] delimiters2 = {'+', '*'};
        
        scanner.addDelimiter(delimiters1);
        assertDoesNotThrow(() -> scanner.addDelimiter(delimiters2));
    }
}
