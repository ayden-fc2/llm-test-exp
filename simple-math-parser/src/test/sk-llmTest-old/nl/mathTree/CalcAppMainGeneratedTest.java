package mathTree;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class CalcAppMainGeneratedTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream capturedOutput;

    @BeforeEach
    public void setUp() {
        capturedOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOutput));
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    // Helper to simulate user input for stdin
    private void provideInput(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        System.setIn(inputStream);
    }

    // Helper to get printed output
    private String getOutput() {
        return capturedOutput.toString().trim();
    }

    // Minimal stub for MathTree assuming it's used as per snippet
    public static class MathTreeStub {
        public boolean init(String expr) {
            if (expr == null || expr.isBlank()) return false;
            // Simulate parsing error on invalid expressions
            if ("invalid".equals(expr)) throw new RuntimeException("Invalid expression");
            return true;
        }

        public double solve() {
            return 42.0; // dummy result
        }
    }

    // Inject stub into CalcApp via reflection since calcTree is assumed static field
    private void injectMathTreeStub() throws Exception {
        Field calcTreeField = CalcApp.class.getDeclaredField("calcTree");
        calcTreeField.setAccessible(true);
        calcTreeField.set(null, new MathTreeStub());
    }

    @Test
    public void test_main_withValidArgument_returnsResult() throws Exception {
        injectMathTreeStub();
        String[] args = {"2+2"};
        CalcApp.main(args);
        assertEquals("42.0", getOutput());
    }

    @Test
    public void test_main_withBlankArgumentFromConsole_readsUntilNonBlankAndReturnsResult() throws Exception {
        injectMathTreeStub();
        provideInput("\n   \n2 + 3\n");
        CalcApp.main(new String[]{});
        assertEquals("42.0", getOutput());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t"})
    public void test_main_withOnlyBlankInputsFromConsole_throwsOrHandlesGracefully(String blankInput) throws Exception {
        injectMathTreeStub();
        provideInput(blankInput + "\n" + blankInput + "\nvalid_expr\n");
        CalcApp.main(new String[]{});
        assertEquals("42.0", getOutput()); // Should eventually process valid_expr
    }

    @Test
    public void test_main_withNullArgument_doesNotCrash() throws Exception {
        injectMathTreeStub();
        String[] args = {null};
        assertDoesNotThrow(() -> CalcApp.main(args));
    }

    @Test
    public void test_main_withInvalidExpression_initFails_noOutput() throws Exception {
        injectMathTreeStub();
        String[] args = {"invalid"};
        assertThrows(RuntimeException.class, () -> CalcApp.main(args));
    }

    @Test
    public void test_main_withNoArgsAndImmediateEOFOnStdin_throwsException() throws Exception {
        injectMathTreeStub();
        provideInput(""); // EOF immediately
        assertThrows(RuntimeException.class, () -> CalcApp.main(new String[]{}));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "1.5", "1e10", "(-5)"})
    public void test_main_withBoundaryNumericsAsArgs_succeeds(String expr) throws Exception {
        injectMathTreeStub();
        CalcApp.main(new String[]{expr});
        assertEquals("42.0", getOutput());
    }
}
