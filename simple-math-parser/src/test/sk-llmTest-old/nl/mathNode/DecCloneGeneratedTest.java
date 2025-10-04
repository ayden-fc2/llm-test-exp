package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Dec#clone()}.
 */
class DecCloneGeneratedTest {

    private Dec decInstance;

    @BeforeEach
    void setUp() {
        decInstance = new Dec();
    }

    @Test
    void test_clone_returnsNonNullInstance() throws CloneNotSupportedException {
        Dec cloned = (Dec) decInstance.clone();
        assertNotNull(cloned);
    }

    @Test
    void test_clone_returnsDifferentInstance() throws CloneNotSupportedException {
        Dec cloned = (Dec) decInstance.clone();
        assertNotSame(decInstance, cloned);
    }

    @Test
    void test_clone_preservesClassType() throws CloneNotSupportedException {
        Dec cloned = (Dec) decInstance.clone();
        assertEquals(Dec.class, cloned.getClass());
    }

    @Test
    void test_clone_onNullInstance_throwsNullPointerException() {
        decInstance = null;
        assertThrows(NullPointerException.class, () -> decInstance.clone());
    }

    @Test
    void test_clone_whenSuperCloneThrowsException_propagatesException() {
        // Create a subclass that overrides clone to throw CloneNotSupportedException
        class UncloneableDec extends Dec {
            @Override
            public Object clone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("Simulated failure");
            }
        }

        UncloneableDec uncloneable = new UncloneableDec();
        assertThrows(CloneNotSupportedException.class, uncloneable::clone);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, 1.0, Double.MAX_VALUE, Double.MIN_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void test_clone_withSpecialDoubleValues(double value) throws CloneNotSupportedException {
        decInstance = new Dec() {
            private double val = value;

            @Override
            public double doubleValue() {
                return val;
            }
        };

        Dec cloned = (Dec) decInstance.clone();
        assertEquals(decInstance.doubleValue(), cloned.doubleValue(), 1e-9);
    }

    @Test
    void test_clone_withNaNValue() throws CloneNotSupportedException {
        decInstance = new Dec() {
            private final double val = Double.NaN;

            @Override
            public double doubleValue() {
                return val;
            }
        };

        Dec cloned = (Dec) decInstance.clone();
        assertTrue(Double.isNaN(cloned.doubleValue()));
    }
}
