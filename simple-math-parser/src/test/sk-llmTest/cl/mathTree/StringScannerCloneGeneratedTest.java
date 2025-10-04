package mathTree;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashSet;
import java.util.LinkedList;
import mathTree.StringScanner;

public class StringScannerCloneGeneratedTest {

    private StringScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new StringScanner();
    }

    @Test
    public void test_clone_createsIndependentCopy() {
        // Setup original scanner with data
        scanner.addDelimiter(";");
        scanner.addSpecialChar('@');
        scanner.tokenize("test;data@here");

        // Clone the scanner
        StringScanner clone = (StringScanner) scanner.clone();

        // Verify clone is not null and is a different object
        assertNotNull(clone);
        assertNotSame(scanner, clone);

        // Verify collections are cloned (different objects)
        assertNotSame(scanner.getTokenList(), clone.getTokenList());
        assertNotSame(scanner.getDelimSet(), clone.getDelimSet());
        assertNotSame(scanner.getSpecCharSet(), clone.getSpecCharSet());

        // Verify contents are equal
        assertEquals(scanner.getTokenList(), clone.getTokenList());
        assertEquals(scanner.getDelimSet(), clone.getDelimSet());
        assertEquals(scanner.getSpecCharSet(), clone.getSpecCharSet());
    }

    @Test
    public void test_clone_withEmptyCollections() {
        // Test cloning when collections are empty
        StringScanner clone = (StringScanner) scanner.clone();

        assertNotNull(clone);
        assertNotSame(scanner, clone);
        assertNotNull(clone.getTokenList());
        assertNotNull(clone.getDelimSet());
        assertNotNull(clone.getSpecCharSet());
        assertTrue(clone.getTokenList().isEmpty());
        assertTrue(clone.getDelimSet().isEmpty());
        assertTrue(clone.getSpecCharSet().isEmpty());
    }

    @Test
    public void test_clone_modifyingCloneDoesNotAffectOriginal() {
        // Setup original
        scanner.addDelimiter(";");
        scanner.addSpecialChar('@');
        scanner.tokenize("test;data");

        // Clone and modify clone
        StringScanner clone = (StringScanner) scanner.clone();
        clone.addDelimiter(",");
        clone.addSpecialChar('#');
        ((LinkedList<String>)clone.getTokenList()).add("extra");

        // Verify original is unchanged
        assertFalse(scanner.getDelimSet().contains(","));
        assertTrue(scanner.getDelimSet().contains(";"));
        assertFalse(scanner.getSpecCharSet().contains('#'));
        assertTrue(scanner.getSpecCharSet().contains('@'));
        assertEquals(2, scanner.getTokenList().size()); // Original should still have 2 tokens
        assertEquals(3, clone.getTokenList().size());  // Clone should now have 3 tokens
    }

    @Test
    public void test_clone_withMultipleDelimitersAndSpecialChars() {
        // Setup with multiple delimiters and special chars
        scanner.addDelimiter(";");
        scanner.addDelimiter(",");
        scanner.addDelimiter("|");
        scanner.addSpecialChar('@');
        scanner.addSpecialChar('#');
        scanner.addSpecialChar('$');
        scanner.tokenize("a;b,c|d@e#f$g");

        StringScanner clone = (StringScanner) scanner.clone();

        assertNotNull(clone);
        assertNotSame(scanner, clone);
        assertEquals(scanner.getDelimSet(), clone.getDelimSet());
        assertEquals(scanner.getSpecCharSet(), clone.getSpecCharSet());
        assertEquals(scanner.getTokenList(), clone.getTokenList());
    }

    @Test
    public void test_clone_returnsCorrectType() {
        Object clone = scanner.clone();
        assertTrue(clone instanceof StringScanner, "Clone should be instance of StringScanner");
    }
}
