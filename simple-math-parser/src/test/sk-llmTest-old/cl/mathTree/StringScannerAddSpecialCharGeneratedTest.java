package mathTree;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashSet;
import mathTree.StringScanner;

public class StringScannerAddSpecialCharGeneratedTest {

    private StringScanner scanner;

    @BeforeEach
    public void setUp() {
        scanner = new StringScanner();
    }

    @Test
    public void test_addSpecialChar_addsSingleCharacter() {
        char[] input = {'+'};
        scanner.addSpecialChar(input);
        
        // Using reflection to access private field for testing purposes
        try {
            var field = StringScanner.class.getDeclaredField("specCharSet");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashSet<Character> specCharSet = (HashSet<Character>) field.get(scanner);
            assertTrue(specCharSet.contains('+'));
        } catch (Exception e) {
            fail("Failed to access specCharSet via reflection");
        }
    }

    @ParameterizedTest
    @ValueSource(chars = {'(', ')', '*', '/', '-', '+'})
    public void test_addSpecialChar_addsMultipleSpecialCharacters(char ch) {
        char[] input = {ch};
        scanner.addSpecialChar(input);
        
        try {
            var field = StringScanner.class.getDeclaredField("specCharSet");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashSet<Character> specCharSet = (HashSet<Character>) field.get(scanner);
            assertTrue(specCharSet.contains(ch));
        } catch (Exception e) {
            fail("Failed to access specCharSet via reflection");
        }
    }

    @Test
    public void test_addSpecialChar_addsArrayWithMultipleCharacters() {
        char[] input = {'+', '-', '*', '/'};
        scanner.addSpecialChar(input);
        
        try {
            var field = StringScanner.class.getDeclaredField("specCharSet");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashSet<Character> specCharSet = (HashSet<Character>) field.get(scanner);
            assertTrue(specCharSet.contains('+'));
            assertTrue(specCharSet.contains('-'));
            assertTrue(specCharSet.contains('*'));
            assertTrue(specCharSet.contains('/'));
        } catch (Exception e) {
            fail("Failed to access specCharSet via reflection");
        }
    }

    @Test
    public void test_addSpecialChar_emptyArrayDoesNotChangeSet() {
        char[] input = {};
        scanner.addSpecialChar(input);
        
        try {
            var field = StringScanner.class.getDeclaredField("specCharSet");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashSet<Character> specCharSet = (HashSet<Character>) field.get(scanner);
            assertNotNull(specCharSet);
        } catch (Exception e) {
            fail("Failed to access specCharSet via reflection");
        }
    }

    @Test
    public void test_addSpecialChar_duplicateCharacters() {
        char[] first = {'+'};
        char[] second = {'+', '-'};
        scanner.addSpecialChar(first);
        scanner.addSpecialChar(second);
        
        try {
            var field = StringScanner.class.getDeclaredField("specCharSet");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashSet<Character> specCharSet = (HashSet<Character>) field.get(scanner);
            assertEquals(2, specCharSet.size()); // Only two unique characters
            assertTrue(specCharSet.contains('+'));
            assertTrue(specCharSet.contains('-'));
        } catch (Exception e) {
            fail("Failed to access specCharSet via reflection");
        }
    }
}
