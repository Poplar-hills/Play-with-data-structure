package Stack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class L20_Valid_ParenthesesTest {
    @Test
    void should_1st_case_return_true() {
        String s = "()";
        L20_Valid_Parentheses r = new L20_Valid_Parentheses();
        assertTrue(r.isValid(s));
    }

    @Test
    void should_2nd_case_return_true() {
        String s = "()[]{}";
        L20_Valid_Parentheses r = new L20_Valid_Parentheses();
        assertTrue(r.isValid(s));
    }

    @Test
    void should_3rd_case_return_false() {
        String s = "(]";
        L20_Valid_Parentheses r = new L20_Valid_Parentheses();
        assertFalse(r.isValid(s));
    }

    @Test
    void should_4th_case_return_false() {
        String s = "([)]";
        L20_Valid_Parentheses r = new L20_Valid_Parentheses();
        assertFalse(r.isValid(s));
    }

    @Test
    void should_5th_case_return_true() {
        String s = "{[]}";
        L20_Valid_Parentheses r = new L20_Valid_Parentheses();
        assertTrue(r.isValid(s));
    }
}