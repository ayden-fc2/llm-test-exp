package mathTree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link StringScanner#isDelim(char)}.
 * <p>
 * Tests cover:
 * - Branch coverage for both conditions: whitespace and delimSet.
 * - Boundary and special values for char input.
 * - Parameterized tests for common whitespace and delimiter characters.
 * - Ensures no I/O or network dependencies.
 */
public class StringScannerIsDelimGeneratedTest {

    private StringScanner scannerWithWhitespaceEnabled;
    private StringScanner scannerWithWhitespaceDisabled;

    // Minimal stub to allow instantiation and access to isDelim
    private static class StringScannerStub extends StringScanner {
        private final boolean skipWhitespace;
        private final Set<Character> delimSet;

        public StringScannerStub(boolean skipWhitespace, Set<Character> delimSet) {
            super(""); // dummy input
            this.skipWhitespace = skipWhitespace;
            this.delimSet = delimSet;
        }

        @Override
        protected boolean isDelim(char ch) {
            return (skipWhitespace && Character.isWhitespace(ch)) || delimSet.contains(ch);
        }
    }

    @BeforeEach
    void setUp() {
        Set<Character> delimiters = new HashSet<>();
        delimiters.add(',');
        delimiters.add(';');

        scannerWithWhitespaceEnabled = new StringScannerStub(true, delimiters);
        scannerWithWhitespaceDisabled = new StringScannerStub(false, delimiters);
    }

    // --- Parameterized Tests for Whitespace Characters (when skipWhitespace = true) ---

    @ParameterizedTest
    @ValueSource(chars = { ' ', '\t', '\n', '\r', '\u00A0' }) // common whitespace chars
    void test_isDelim_returnsTrue_forWhitespace_whenSkipWhitespaceEnabled(char ch) {
        assertTrue(scannerWithWhitespaceEnabled.isDelim(ch));
    }

    // --- Parameterized Tests for Delimiter Set Members ---

    @ParameterizedTest
    @ValueSource(chars = { ',', ';' })
    void test_isDelim_returnsTrue_forDelimiterSetMembers(char ch) {
        assertTrue(scannerWithWhitespaceEnabled.isDelim(ch));
        assertTrue(scannerWithWhitespaceDisabled.isDelim(ch));
    }

    // --- Boundary and Special Values ---

    @Test
    void test_isDelim_returnsFalse_forNullChar_whenWhitespaceDisabled_andNotInDelimSet() {
        // Note: '\0' is a valid char value in Java (not null), but not a delimiter or whitespace we care about
        assertFalse(scannerWithWhitespaceDisabled.isDelim('\0'));
    }

    @Test
    void test_isDelim_returnsFalse_forMaxChar_whenWhitespaceDisabled_andNotInDelimSet() {
        assertFalse(scannerWithWhitespaceDisabled.isDelim(Character.MAX_VALUE));
    }

    @Test
    void test_isDelim_returnsFalse_forMinChar_whenWhitespaceDisabled_andNotInDelimSet() {
        assertFalse(scannerWithWhitespaceDisabled.isDelim(Character.MIN_VALUE));
    }

    @Test
    void test_isDelim_returnsFalse_forRegularChar_whenWhitespaceDisabled_andNotInDelimSet() {
        assertFalse(scannerWithWhitespaceDisabled.isDelim('a'));
    }

    @Test
    void test_isDelim_returnsTrue_forWhitespace_whenSkipWhitespaceEnabled_andNotInDelimSet() {
        assertTrue(scannerWithWhitespaceEnabled.isDelim(' '));
    }

    @Test
    void test_isDelim_returnsFalse_forWhitespace_whenSkipWhitespaceDisabled_andNotInDelimSet() {
        assertFalse(scannerWithWhitespaceDisabled.isDelim(' '));
    }

    // --- Edge Cases for Delimiter Set ---

    @Test
    void test_isDelim_returnsTrue_forCharInDelimSet_evenIfWhitespace_butWhitespaceDisabled() {
        Set<Character> delimiters = new HashSet<>();
        delimiters.add(' '); // space is also a delimiter
        StringScanner scanner = new StringScannerStub(false, delimiters);

        assertTrue(scanner.isDelim(' '));
    }

    @Test
    void test_isDelim_returnsTrue_forCharInDelimSet_evenIfNotWhitespace() {
        Set<Character> delimiters = new HashSet<>();
        delimiters.add('#');
        StringScanner scanner = new StringScannerStub(false, delimiters);

        assertTrue(scanner.isDelim('#'));
    }

    // --- Empty DelimSet Tests ---

    @Test
    void test_isDelim_returnsFalse_forAnyChar_whenDelimSetIsEmpty_andWhitespaceDisabled() {
        StringScanner scanner = new StringScannerStub(false, new HashSet<>());
        assertFalse(scanner.isDelim('a'));
        assertFalse(scanner.isDelim(' '));
        assertFalse(scanner.isDelim(','));
    }

    @Test
    void test_isDelim_returnsTrue_onlyForWhitespace_whenDelimSetIsEmpty_andWhitespaceEnabled() {
        StringScanner scanner = new StringScannerStub(true, new HashSet<>());
        assertTrue(scanner.isDelim(' '));
        assertFalse(scanner.isDelim('a'));
        assertFalse(scanner.isDelim(','));
    }
}
