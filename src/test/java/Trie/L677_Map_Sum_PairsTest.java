package Trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class L677_Map_Sum_PairsTest {
    private L677_Map_Sum_Pairs r;

    @BeforeEach
    void init() {
        r = new L677_Map_Sum_Pairs();
    }

    @Test
    void should_return_the_sum_of_words_with_the_same_prefix() {
        r.insert("apple", 3);
        assertEquals(3, r.sum("ap"));
        r.insert("app", 2);
        assertEquals(5, r.sum("ap"));
        r.insert("cap", 10);  // "cap" is not prefixed with "ap", should not count
        assertEquals(5, r.sum("ap"));
        r.insert("apply", 1);
        assertEquals(6, r.sum("ap"));
    }

    @Test
    void should_override_the_value_if_the_key_already_exist_when_overriding() {
        r.insert("apple", 3);
        assertEquals(3, r.sum("ap"));
        r.insert("apple", 2);
        assertEquals(2, r.sum("ap"));
    }
}