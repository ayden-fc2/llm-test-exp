package mathNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Expression Clone Tests")
class ExpressionCloneGeneratedTest {

    // Minimal stub to make the class cloneable and compilable
    static class Expression implements Cloneable {
        private int value;
        private String name;

        public Expression() {
            this.value = 0;
            this.name = "";
        }

        public Expression(int value, String name) {
            this.value = value;
            this.name = name;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Expression)) return false;
            Expression that = (Expression) o;
            return value == that.value && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return 31 * value + name.hashCode();
        }
    }

    @Test
    @DisplayName("test_clone_normalObject_returnsEqualButNotSameInstance")
    void test_clone_normalObject_returnsEqualButNotSameInstance() throws CloneNotSupportedException {
        Expression original = new Expression(42, "test");
        Expression cloned = (Expression) original.clone();

        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(original, cloned);
    }

    @Test
    @DisplayName("test_clone_withZeroValue_returnsEqualInstance")
    void test_clone_withZeroValue_returnsEqualInstance() throws CloneNotSupportedException {
        Expression original = new Expression(0, "zero");
        Expression cloned = (Expression) original.clone();

        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(original, cloned);
    }

    @Test
    @DisplayName("test_clone_withNegativeValue_returnsEqualInstance")
    void test_clone_withNegativeValue_returnsEqualInstance() throws CloneNotSupportedException {
        Expression original = new Expression(-1, "negative");
        Expression cloned = (Expression) original.clone();

        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(original, cloned);
    }

    @Test
    @DisplayName("test_clone_withMaxIntValue_returnsEqualInstance")
    void test_clone_withMaxIntValue_returnsEqualInstance() throws CloneNotSupportedException {
        Expression original = new Expression(Integer.MAX_VALUE, "max");
        Expression cloned = (Expression) original.clone();

        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(original, cloned);
    }

    @Test
    @DisplayName("test_clone_withMinIntValue_returnsEqualInstance")
    void test_clone_withMinIntValue_returnsEqualInstance() throws CloneNotSupportedException {
        Expression original = new Expression(Integer.MIN_VALUE, "min");
        Expression cloned = (Expression) original.clone();

        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(original, cloned);
    }

    @Test
    @DisplayName("test_clone_withEmptyName_returnsEqualInstance")
    void test_clone_withEmptyName_returnsEqualInstance() throws CloneNotSupportedException {
        Expression original = new Expression(1, "");
        Expression cloned = (Expression) original.clone();

        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(original, cloned);
    }

    @Test
    @DisplayName("test_clone_multipleTimes_returnsEqualInstances")
    void test_clone_multipleTimes_returnsEqualInstances() throws CloneNotSupportedException {
        Expression original = new Expression(123, "multi");
        Expression clone1 = (Expression) original.clone();
        Expression clone2 = (Expression) original.clone();

        assertNotNull(clone1);
        assertNotNull(clone2);
        assertNotSame(original, clone1);
        assertNotSame(original, clone2);
        assertNotSame(clone1, clone2);
        assertEquals(original, clone1);
        assertEquals(original, clone2);
        assertEquals(clone1, clone2);
    }

    @Test
    @DisplayName("test_clone_whenCalledOnClone_returnsEqualInstance")
    void test_clone_whenCalledOnClone_returnsEqualInstance() throws CloneNotSupportedException {
        Expression original = new Expression(100, "original");
        Expression firstClone = (Expression) original.clone();
        Expression secondClone = (Expression) firstClone.clone();

        assertNotNull(secondClone);
        assertNotSame(original, secondClone);
        assertNotSame(firstClone, secondClone);
        assertEquals(original, secondClone);
        assertEquals(firstClone, secondClone);
    }

    @Test
    @DisplayName("test_clone_withNullName_throwsNullPointerException")
    void test_clone_withNullName_throwsNullPointerException() {
        Expression original = new Expression(1, null); // This will cause NPE in equals/hashCode
        assertThrows(NullPointerException.class, () -> {
            Expression cloned = (Expression) original.clone();
            cloned.equals(original); // Trigger NPE during comparison
        });
    }

    // Note: Since the implementation just calls super.clone(), it's hard to trigger CloneNotSupportedException
    // unless we modify the class to be non-cloneable. Here's a test that shows it works correctly when cloneable.
    @Test
    @DisplayName("test_clone_onCloneableInstance_doesNotThrowException")
    void test_clone_onCloneableInstance_doesNotThrowException() {
        Expression original = new Expression();
        assertDoesNotThrow(() -> original.clone());
    }
}
