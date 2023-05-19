import except.NormalStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalStackTest {

    private NormalStack<Integer> s;

    @BeforeEach
    void setup() {
        s = new NormalStack<>();
    }

    @Test
    void isInitiallyEmpty() {
        assertTrue(s.isEmpty());
    }

    @Test
    void isNotEmptyAfterPush() {
        s.push(1);
        assertFalse(s.isEmpty());
    }

    @Test
    void isEmptyAfterPushAndPop() {
        s.push(1);
        s.pop();
        assertTrue(s.isEmpty());
    }

    @Test
    void isEmptyAfterCorrectNumberOfPops() {
        for (int i = 0; i < 10; i++) {
            s.push(2);
        }
        for (int i = 0; i < 9; i++) {
            s.pop();
        }
        assertFalse(s.isEmpty());
        s.pop();
        assertTrue(s.isEmpty());
    }

    @Test
    void popsKeysInCorrectOrder() {
        for (int i = 0; i < 5; i++) {
            s.push(i);
        }
        for (int i = 4; i >= 0; i--) {
            assertEquals(i, s.pop());
        }
    }

    @Test
    void returnsNullWhenEmptyStackIsPopped() {
        assertNull(s.pop());
    }

}