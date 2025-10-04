package mathNode;

import mathNode.Int;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class IntCloneGeneratedTest {

    @Test
    @DisplayName("test_clone_creates_deep_copy")
    public void test_clone_creates_deep_copy() {
        // Arrange
        Int original = new Int(42);
        
        // Act
        Int cloned = (Int) original.clone();
        
        // Assert
        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(original.getValue(), cloned.getValue());
        assertTrue(cloned instanceof Int);
    }

    @Test
    @DisplayName("test_clone_with_zero_value")
    public void test_clone_with_zero_value() {
        // Arrange
        Int original = new Int(0);
        
        // Act
        Int cloned = (Int) original.clone();
        
        // Assert
        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(0, cloned.getValue());
    }

    @Test
    @DisplayName("test_clone_with_negative_value")
    public void test_clone_with_negative_value() {
        // Arrange
        Int original = new Int(-42);
        
        // Act
        Int cloned = (Int) original.clone();
        
        // Assert
        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(-42, cloned.getValue());
    }

    @Test
    @DisplayName("test_clone_with_max_integer_value")
    public void test_clone_with_max_integer_value() {
        // Arrange
        Int original = new Int(Integer.MAX_VALUE);
        
        // Act
        Int cloned = (Int) original.clone();
        
        // Assert
        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(Integer.MAX_VALUE, cloned.getValue());
    }

    @Test
    @DisplayName("test_clone_with_min_integer_value")
    public void test_clone_with_min_integer_value() {
        // Arrange
        Int original = new Int(Integer.MIN_VALUE);
        
        // Act
        Int cloned = (Int) original.clone();
        
        // Assert
        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(Integer.MIN_VALUE, cloned.getValue());
    }

    @Test
    @DisplayName("test_checkTree_returns_true")
    public void test_checkTree_returns_true() {
        // Arrange
        Int node = new Int(10);
        
        // Act
        boolean result = node.checkTree();
        
        // Assert
        assertTrue(result);
    }
}
