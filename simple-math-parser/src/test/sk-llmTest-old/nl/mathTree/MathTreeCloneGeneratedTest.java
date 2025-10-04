package mathTree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MathTreeCloneGeneratedTest {

    // Minimal stubs to support compilation and testing
    static class mathNode {
        static class Factory implements Cloneable {
            private final String id;

            Factory(String id) {
                this.id = id;
            }

            @Override
            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof Factory)) return false;
                return this.id.equals(((Factory) obj).id);
            }
        }

        static abstract class Expression implements Cloneable {
            protected String value;

            Expression(String value) {
                this.value = value;
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof Expression)) return false;
                return this.value.equals(((Expression) obj).value);
            }
        }

        static class ConstantExpression extends Expression {
            ConstantExpression(String value) {
                super(value);
            }

            @Override
            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }
    }

    private MathTree mathTree;
    private mathNode.Factory factoryStub;
    private mathNode.Expression expressionStub;

    @BeforeEach
    void setUp() {
        factoryStub = new mathNode.Factory("testFactory");
        expressionStub = new mathNode.ConstantExpression("testExpression");
        mathTree = new MathTree();
        mathTree.nodeFactory = factoryStub;
        mathTree.rootNode = expressionStub;
    }

    @Test
    void test_clone_normalConditions_createsDeepCopy() throws CloneNotSupportedException {
        MathTree cloned = (MathTree) mathTree.clone();

        assertNotNull(cloned);
        assertNotSame(mathTree, cloned);
        assertNotSame(mathTree.nodeFactory, cloned.nodeFactory);
        assertNotSame(mathTree.rootNode, cloned.rootNode);

        assertEquals(mathTree.nodeFactory, cloned.nodeFactory);
        assertEquals(mathTree.rootNode, cloned.rootNode);
    }

    @Test
    void test_clone_whenNodeFactoryIsNull_throwsNullPointerException() {
        mathTree.nodeFactory = null;

        assertThrows(NullPointerException.class, () -> mathTree.clone());
    }

    @Test
    void test_clone_whenRootNodeIsNull_throwsNullPointerException() {
        mathTree.rootNode = null;

        assertThrows(NullPointerException.class, () -> mathTree.clone());
    }

    @Test
    void test_clone_multipleTimes_eachCloneIsIndependent() throws CloneNotSupportedException {
        MathTree clone1 = (MathTree) mathTree.clone();
        MathTree clone2 = (MathTree) mathTree.clone();

        assertNotSame(clone1, clone2);
        assertNotSame(clone1.nodeFactory, clone2.nodeFactory);
        assertNotSame(clone1.rootNode, clone2.rootNode);

        assertEquals(clone1.nodeFactory, clone2.nodeFactory);
        assertEquals(clone1.rootNode, clone2.rootNode);
    }

    @Test
    void test_clone_verifyCloneType_isMathTreeInstance() throws CloneNotSupportedException {
        MathTree cloned = (MathTree) mathTree.clone();
        assertTrue(cloned instanceof MathTree);
    }

    // Additional edge case: empty values in nodes
    @Test
    void test_clone_withEmptyNodeValue_createsCopy() throws CloneNotSupportedException {
        mathTree.nodeFactory = new mathNode.Factory("");
        mathTree.rootNode = new mathNode.ConstantExpression("");

        MathTree cloned = (MathTree) mathTree.clone();

        assertNotNull(cloned);
        assertEquals(mathTree.nodeFactory, cloned.nodeFactory);
        assertEquals(mathTree.rootNode, cloned.rootNode);
    }
}
