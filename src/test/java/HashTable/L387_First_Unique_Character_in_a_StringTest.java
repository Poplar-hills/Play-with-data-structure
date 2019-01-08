package HashTable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class L387_First_Unique_Character_in_a_StringTest {
    private L387_First_Unique_Character_in_a_String r;

    @BeforeEach
    void init() { r = new L387_First_Unique_Character_in_a_String(); }

    @Test
    void should_return_the_first_unique_char_in_a_string_1() {
        String s = "leetcode";
        assertEquals(0, r.firstUniqChar(s));
    }

    @Test
    void should_return_the_first_unique_char_in_a_string_2() {
        String s = "loveleetcode";
        assertEquals(2, r.firstUniqChar(s));
    }
}