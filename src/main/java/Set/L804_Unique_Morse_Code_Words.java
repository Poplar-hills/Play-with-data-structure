package Set;

import java.util.TreeSet;

public class L804_Unique_Morse_Code_Words {
    public int uniqueMorseRepresentations(String[] words) {
        String[] codes = {".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."};
        TreeSet<String> set = new TreeSet<String>();

        for (String word : words) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < word.length(); i++)  // encode each character
                s.append(codes[word.charAt(i) - 'a']);  // charAt 返回i所在位置的字符，'a'是偏移量，相减得到 0-25 之间的索引

            set.add(s.toString());  // save each word in the set
        }

        return set.size();
    }
}
