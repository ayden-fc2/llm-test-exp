package mathTree;

import mathTree.CalcApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class CalcAppMainGeneratedTest {

    @TempDir
    static Path tempDir;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Test
    public void test_main_withNoArgs_promptsForInput() {
        // Arrange
        String input = "2 + 3\n";
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));

            // Act & Assert
            assertDoesNotThrow(() -> CalcApp.main(new String[]{}));
            assertTrue(outContent.toString().contains("Enter a mathematical expression"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    public void test_main_withEmptyOrWhitespaceInput(String input) {
        // Arrange
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));

            // Act & Assert
            assertDoesNotThrow(() -> CalcApp.main(new String[]{}));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }

    @Test
    public void test_main_withValidExpressionAsArg_returnsResult() {
        // Arrange
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Act
        assertDoesNotThrow(() -> CalcApp.main(new String[]{"2+3"}));

        // Assert
        assertTrue(outContent.toString().contains("5"));
    }

    @Test
    public void test_main_withZeroInExpression() {
        // Arrange
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Act
        assertDoesNotThrow(() -> CalcApp.main(new String[]{"0*5"}));

        // Assert
        assertTrue(outContent.toString().contains("0"));
    }

    @Test
    public void test_main_withNegativeNumbers() {
        // Arrange
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Act
        assertDoesNotThrow(() -> CalcApp.main(new String[]{"-2+-3"}));

        // Assert
        assertTrue(outContent.toString().contains("-5"));
    }

    @Test
    public void test_main_withMaxIntValues() {
        // Arrange
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Act
        assertDoesNotThrow(() -> CalcApp.main(new String[]{String.valueOf(Integer.MAX_VALUE) + "+" + "0"}));

        // Assert
        assertTrue(outContent.toString().contains(String.valueOf(Integer.MAX_VALUE)));
    }

    @Test
    public void test_main_withMinIntValues() {
        // Arrange
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Act
        assertDoesNotThrow(() -> CalcApp.main(new String[]{String.valueOf(Integer.MIN_VALUE) + "+" + "0"}));

        // Assert
        assertTrue(outContent.toString().contains(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void test_main_withVeryLargeDouble() {
        // Arrange
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Act
        assertDoesNotThrow(() -> CalcApp.main(new String[]{"1.7976931348623157E308+0"}));

        // Assert
        assertTrue(outContent.toString().contains("1.7976931348623157E308"));
    }

    @Test
    public void test_main_withVerySmallDouble() {
        // Arrange
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Act
        assertDoesNotThrow(() -> CalcApp.main(new String[]{"4.9E-324+0"}));

        // Assert
        assertTrue(outContent.toString().contains("4.9E-324"));
    }
}
