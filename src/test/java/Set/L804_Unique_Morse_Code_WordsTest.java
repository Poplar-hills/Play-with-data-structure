package Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class L804_Unique_Morse_Code_WordsTest {
    @Test
    void should_1st_case_return_true() {
        String[] words = {"gin", "zen", "gig", "msg"};
        L804_Unique_Morse_Code_Words r = new L804_Unique_Morse_Code_Words();
        assertEquals(r.uniqueMorseRepresentations(words), 2);
    }
}
