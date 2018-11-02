package Trie;

/*          root
 *        /     \
 *       d      c
 *      / |      \
 *     o  e       a
 *    /   |      / \
 *   g*   e     m   t*
 *        |     |    \
 *        r*    e     t
 *              |      \
 *              l*      l
 *                       \
 *                        e*
 * */

public class TrieTest {
    public static void main(String[] args) {
        Trie trie = new Trie();

        System.out.println("Testing add...");
        trie.add("dog");
        trie.add("deer");
        trie.add("cat");
        trie.add("cattle");
        trie.add("camel");

        System.out.println(trie.contains("cat"));
        System.out.println(trie.contains("camel"));
        System.out.println(trie.contains("cattle"));

        System.out.println("\nTesting delete...");
        trie.delete("cattle");
        trie.delete("deer");

        System.out.println(trie.contains("dog"));
        System.out.println(trie.contains("deer"));
        System.out.println(trie.contains("camel"));
        System.out.println(trie.contains("cat"));
        System.out.println(trie.contains("cattle"));

        System.out.println("\nTesting startWith...");
        System.out.println(trie.startWith("ca"));
        System.out.println(trie.startWith("dd"));
    }
}
