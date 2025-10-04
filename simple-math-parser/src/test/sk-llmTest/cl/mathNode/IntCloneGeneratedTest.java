package mathNode;

import mathNode.Int;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class IntCloneGeneratedTest {

    @Test
    void test_clone_returnsNewInstanceWithSameValue() {
        // Arrange
        int value = 42;
        Int original = new Int(value);
        
        // Act
        Int cloned = (Int) original.clone();
        
        // Assert
        assertNotNull(cloned, "Cloned object should not be null");
        assertNotSame(original, cloned, "Clone should be a different instance");
        assertEquals(original.getValue(), cloned.getValue(), "Clone should have the same value as original");
        assertTrue(cloned instanceof Int, "Clone should be an instance of Int");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void test_clone_withEdgeIntegerValues(int value) {
        // Arrange
        Int original = new Int(value);
        
        // Act
        Int cloned = (Int) original.clone();
        
        // Assert
        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(value, cloned.getValue());
        assertTrue(cloned instanceof Int);
    }

    @Test
    void test_checkTree_alwaysReturnsTrue() {
        // Arrange
        Int node = new Int(10);
        
        // Act
        boolean result = node.checkTree();
        
        // Assert
        assertTrue(result, "checkTree should always return true for Int nodes");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -100, 100, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void test_checkTree_withVariousIntValues(int value) {
        // Arrange
        Int node = new Int(value);
        
        // Act
        boolean result = node.checkTree();
        
        // Assert
        assertTrue(result);
    }
}
