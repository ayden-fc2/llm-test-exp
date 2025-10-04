package mathTree;

import mathTree.StringScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StringScannerIsDelimGeneratedTest {

    private StringScanner scanner;
    private Field delimSetField;
    private Field skipWhitespaceField;

    @BeforeEach
    public void setUp() throws Exception {
        scanner = new StringScanner("");
        delimSetField = StringScanner.class.getDeclaredField("delimSet");
        delimSetField.setAccessible(true);
        skipWhitespaceField = StringScanner.class.getDeclaredField("skipWhitespace");
        skipWhitespaceField.setAccessible(true);
    }

    @Test
    public void test_isDelim_whenSkipWhitespaceTrueAndCharIsWhitespace_returnsTrue() throws Exception {
        // Arrange
        skipWhitespaceField.set(scanner, true);
        char ch = ' ';

        // Act
        boolean result = scanner.isDelim(ch);

        // Assert
        assertTrue(result, "Expected whitespace character to be delimiter when skipWhitespace is true");
    }

    @Test
    public void test_isDelim_whenSkipWhitespaceFalseAndCharIsWhitespace_returnsFalse() throws Exception {
        // Arrange
        skipWhitespaceField.set(scanner, false);
        char ch = '\t';

        // Act
        boolean result = scanner.isDelim(ch);

        // Assert
        assertFalse(result, "Expected whitespace character to not be delimiter when skipWhitespace is false");
    }

    @Test
    public void test_isDelim_whenCharInDelimSet_returnsTrue() throws Exception {
        // Arrange
        skipWhitespaceField.set(scanner, false);
        Set<Character> delimSet = Set.of(',', ';', ':');
        delimSetField.set(scanner, delimSet);
        char ch = ',';

        // Act
        boolean result = scanner.isDelim(ch);

        // Assert
        assertTrue(result, "Expected character in delimSet to be recognized as delimiter");
    }

    @Test
    public void test_isDelim_whenCharNotInDelimSetAndNotWhitespace_returnsFalse() throws Exception {
        // Arrange
        skipWhitespaceField.set(scanner, false);
        Set<Character> delimSet = Set.of(',', ';', ':');
        delimSetField.set(scanner, delimSet);
        char ch = 'a';

        // Act
        boolean result = scanner.isDelim(ch);

        // Assert
        assertFalse(result, "Expected character not in delimSet and not whitespace to not be delimiter");
    }

    @ParameterizedTest
    @ValueSource(chars = {' ', '\t', '\n', '\r'})
    public void test_isDelim_withVariousWhitespaceChars_whenSkipWhitespaceTrue_returnsTrue(char ch) throws Exception {
        // Arrange
        skipWhitespaceField.set(scanner, true);

        // Act
        boolean result = scanner.isDelim(ch);

        // Assert
        assertTrue(result, "Expected whitespace character '" + ch + "' to be delimiter when skipWhitespace is true");
    }

    @ParameterizedTest
    @ValueSource(chars = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'})
    public void test_isDelim_withNonDelimiterChars_whenSkipWhitespaceFalse_returnsFalse(char ch) throws Exception {
        // Arrange
        skipWhitespaceField.set(scanner, false);
        delimSetField.set(scanner, Set.of());

        // Act
        boolean result = scanner.isDelim(ch);

        // Assert
        assertFalse(result, "Expected non-delimiter character '" + ch + "' to not be delimiter when skipWhitespace is false");
    }

    @Test
    public void test_isDelim_withNullDelimSet_throwsException() throws Exception {
        // Arrange
        skipWhitespaceField.set(scanner, false);
        delimSetField.set(scanner, null);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> scanner.isDelim('a'));
    }

    @Test
    public void test_isDelim_edgeCase_zeroChar_returnsFalse() throws Exception {
        // Arrange
        skipWhitespaceField.set(scanner, false);
        delimSetField.set(scanner, Set.of());
        char ch = '\0';

        // Act
        boolean result = scanner.isDelim(ch);

        // Assert
        assertFalse(result, "Expected null character to not be delimiter");
    }

    @Test
    public void test_isDelim_whenBothConditionsMet_returnsTrue() throws Exception {
        // Arrange
        skipWhitespaceField.set(scanner, true);
        Set<Character> delimSet = Set.of('x');
        delimSetField.set(scanner, delimSet);
        char ch = 'x';

        // Act
        boolean result = scanner.isDelim(ch);

        // Assert
        assertTrue(result, "Expected character in delimSet to be delimiter regardless of skipWhitespace setting");
    }
}
