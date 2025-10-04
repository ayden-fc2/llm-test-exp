package mathTree;

import mathTree.StringScanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class StringScannerSkipWhitespaceGeneratedTest {

    private StringScanner scanner;

    @BeforeEach
    public void setUp() {
        scanner = new StringScanner();
    }

    @Test
    public void test_skipWhitespace_defaultState() {
        // Verify default state before calling skipWhitespace
        assertFalse(scanner.skipWhitespace, "skipWhitespace should be false by default");
        
        // Call the method under test
        scanner.skipWhitespace();
        
        // Verify postcondition
        assertTrue(scanner.skipWhitespace, "skipWhitespace should be true after calling skipWhitespace()");
    }

    @Test
    public void test_skipWhitespace_multipleCalls() {
        // Initial state
        assertFalse(scanner.skipWhitespace, "skipWhitespace should be false by default");
        
        // First call
        scanner.skipWhitespace();
        assertTrue(scanner.skipWhitespace, "skipWhitespace should be true after first call");
        
        // Second call (testing idempotency)
        scanner.skipWhitespace();
        assertTrue(scanner.skipWhitespace, "skipWhitespace should remain true after second call");
    }

    @Test
    public void test_skipWhitespace_withDelimitersAdded() {
        // Add some delimiters first
        scanner.addDelimiter(' ');
        scanner.addDelimiter(new char[]{',', ';'});
        
        // Verify skipWhitespace still works correctly
        assertFalse(scanner.skipWhitespace, "skipWhitespace should be false by default even with delimiters");
        scanner.skipWhitespace();
        assertTrue(scanner.skipWhitespace, "skipWhitespace should be true after calling skipWhitespace() even with delimiters");
    }

    @Test
    public void test_addDelimiter_singleCharacter() {
        // Initially no delimiters
        assertFalse(scanner.delimSet.contains('a'), "Delimiter 'a' should not be present initially");
        
        // Add a delimiter
        scanner.addDelimiter('a');
        
        // Verify it was added
        assertTrue(scanner.delimSet.contains('a'), "Delimiter 'a' should be present after adding");
        assertEquals(1, scanner.delimSet.size(), "Only one delimiter should be present");
    }

    @Test
    public void test_addDelimiter_multipleCharacters() {
        char[] delimiters = {' ', ',', ';', '\t'};
        
        // Initially no delimiters
        for (char c : delimiters) {
            assertFalse(scanner.delimSet.contains(c), "Delimiter '" + c + "' should not be present initially");
        }
        
        // Add each delimiter
        for (char c : delimiters) {
            scanner.addDelimiter(c);
        }
        
        // Verify all were added
        for (char c : delimiters) {
            assertTrue(scanner.delimSet.contains(c), "Delimiter '" + c + "' should be present after adding");
        }
        assertEquals(delimiters.length, scanner.delimSet.size(), "All delimiters should be present");
    }

    @Test
    public void test_addDelimiter_arrayOfCharacters() {
        char[] delimiters = {'(', ')', '[', ']', '{', '}'};
        
        // Initially no delimiters
        for (char c : delimiters) {
            assertFalse(scanner.delimSet.contains(c), "Delimiter '" + c + "' should not be present initially");
        }
        
        // Add array of delimiters
        scanner.addDelimiter(delimiters);
        
        // Verify all were added
        for (char c : delimiters) {
            assertTrue(scanner.delimSet.contains(c), "Delimiter '" + c + "' should be present after adding array");
        }
        assertEquals(delimiters.length, scanner.delimSet.size(), "All delimiters from array should be present");
    }

    @Test
    public void test_addDelimiter_duplicateCharacters() {
        // Add same character twice
        scanner.addDelimiter('x');
        scanner.addDelimiter('x');
        
        // Should only contain one instance
        assertEquals(1, scanner.delimSet.size(), "Should only contain one instance of duplicate delimiter");
        assertTrue(scanner.delimSet.contains('x'), "Delimiter 'x' should be present");
    }

    @Test
    public void test_addDelimiter_emptyArray() {
        char[] emptyArray = {};
        scanner.addDelimiter(emptyArray);
        
        // No change expected
        assertEquals(0, scanner.delimSet.size(), "No delimiters should be added from empty array");
    }

    @Test
    public void test_addDelimiter_specialCharacters() {
        char[] specialChars = {'\n', '\r', '\0', (char) 127};
        
        // Add special characters
        scanner.addDelimiter(specialChars);
        
        // Verify they were added
        for (char c : specialChars) {
            assertTrue(scanner.delimSet.contains(c), "Special character '" + c + "' should be present");
        }
        assertEquals(specialChars.length, scanner.delimSet.size(), "All special characters should be present");
    }
}
