package Set;

import java.util.TreeSet;

public class L804_Unique_Morse_Code_Words {
    public int uniqueMorseRepresentations(String[] words) {
        String[] codes = {".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."};
        TreeSet<String> set = new TreeSet<String>();

        for (String word : words) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < word.length(); i++)  // encode each character
                s.append(codes[word.charAt(i) - 'a']);  // charAt 返回i所在位置字符的 ASMII 码；'a'是偏移量

            set.add(s.toString());  // save each word in the set
        }

        return set.size();
    }
}
