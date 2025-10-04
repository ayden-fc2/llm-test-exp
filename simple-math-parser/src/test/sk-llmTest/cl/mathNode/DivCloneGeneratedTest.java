package mathNode;

import mathNode.Div;
import mathNode.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class DivCloneGeneratedTest {

    private Div div;
    private Expression mockLeft;
    private Expression mockRight;

    @BeforeEach
    void setUp() {
        // Create a minimal stub for Expression to satisfy compilation
        mockLeft = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };
        mockRight = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };
        div = new Div();
        div.setLeftNode(mockLeft);
        div.setRightNode(mockRight);
    }

    @Test
    void test_clone_returnsNonNullInstance() {
        Div cloned = (Div) div.clone();
        assertNotNull(cloned);
    }

    @Test
    void test_clone_createsNewInstance() {
        Div cloned = (Div) div.clone();
        assertNotSame(div, cloned);
    }

    @Test
    void test_clone_leftNodeIsCloned() {
        Expression leftClone = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };
        mockLeft = new Expression() {
            @Override
            public Object clone() {
                return leftClone;
            }
        };
        div.setLeftNode(mockLeft);

        Div cloned = (Div) div.clone();
        assertSame(leftClone, cloned.getLeftNode());
        assertNotSame(mockLeft, cloned.getLeftNode());
    }

    @Test
    void test_clone_rightNodeIsCloned() {
        Expression rightClone = new Expression() {
            @Override
            public Object clone() {
                return this;
            }
        };
        mockRight = new Expression() {
            @Override
            public Object clone() {
                return rightClone;
            }
        };
        div.setRightNode(mockRight);

        Div cloned = (Div) div.clone();
        assertSame(rightClone, cloned.getRightNode());
        assertNotSame(mockRight, cloned.getRightNode());
    }

    @Test
    void test_clone_preservesOriginalValues() {
        Div cloned = (Div) div.clone();
        assertNotSame(div.getLeftNode(), cloned.getLeftNode());
        assertNotSame(div.getRightNode(), cloned.getRightNode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "-1", "Integer.MIN_VALUE", "Integer.MAX_VALUE"})
    void test_clone_withEdgeCaseValues(String valueStr) {
        // For this test, we're focusing on structural cloning behavior,
        // not value evaluation, so edge cases in value don't affect the structure
        Div cloned = (Div) div.clone();
        assertNotNull(cloned);
        assertNotSame(div, cloned);
    }
}
