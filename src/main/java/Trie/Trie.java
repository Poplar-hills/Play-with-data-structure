package Trie;

/*
* - Trie（字典树），通常只用于处理字符串。
* - 对于查询操作，使用平衡二叉树的复杂度是 O(logn)，这其实已经很高效了，但是对于百万级（2^20）的数据量，logn 大约是20；
*   而使用 Trie 的话，查询的时间复杂度跟数据条数无关！只跟要查询的字符串的长度 w 相关，即 O(w)，而大对数英语单词的
*   长度都在10以下。
* - 另一个影响性能的因素是 Trie 中节点的 next 是使用哪种 map 实现，如果使用 java 的 TreeMap（底层是红黑树）则会比使用
*   HashMap 差一些。
* - 一个存有 dog, deer, cat, cattle, camel 的 Trie 长这样：
*          root
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
* - Trie 的每个节点可能有多个子节点，即 Trie 是多叉树。如果是英文，则每个节点至少有26个子节点，而至多则有
*   26小写 + 26大写 + n个标点符号个子节点。
* - 要实现这样的数据结构需要先定义清楚每一个节点。Trie 的节点具有如下性质：
*   1. 可动态添加删除
*   2. 每个节点维护一个以字符为 key、节点为 value 的 map（节点实际上不保存字符，节点之间的边才是字符）
*   3. 每个节点维护一个 bool 值，用于标记该节点对应的字母是否是一个单词的结尾（如 cat 在 camel、cattle 中，
*      因此 t 对应的节点需要标记为单词末尾）
* - Trie 也叫做"前缀树"，因为 Trie 可以非常方便地查看当前存储的所有单词中是否有某个前缀对应的单词。
* */

import java.util.HashMap;

public class Trie {
    private Node root;
    private int size;

    private class Node {
        public boolean isEndOfWord;
        public HashMap<Character, Node> next;

        public Node(boolean isEndOfWord) {
            this.isEndOfWord = isEndOfWord;
            next = new HashMap<>();
        }

        public Node() { this(false); }
    }

    public Trie() {
        size = 0;
        root = new Node();
    }

    /*
     * 增操作
     * */
    public void add(String word) {  // 增加一个单词到 Trie 中
        add(root, word, 0);
    }

    private void add(Node node, String word, int index) {
        if (index == word.length()) {  // 注意此处不是 word.length() - 1，因为此时的 node 是最后一个字母对应的节点，而非维护包含最后一个字母的 map 的节点
            if (!node.isEndOfWord) {
                node.isEndOfWord = true;
                size++;
            }
            return;
        }

        char c = word.charAt(index);
        if (!node.next.containsKey(c))
            node.next.put(c, new Node());
        add(node.next.get(c), word, index + 1);
    }

    public void addNR(String word) {  // 非递归实现
        Node curr = root;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!curr.next.containsKey(c))
                curr.next.put(c, new Node());
            curr = curr.next.get(c);
        }

        if (!curr.isEndOfWord) {  // 该单词结尾没有被标记过（即该单词不存在于该 trie 中）
            curr.isEndOfWord = true;
            size++;
        }
    }

    /*
     * 删操作
     * */
    public void delete(String word) {
        delete(root, word, 0);
    }

    private boolean delete(Node node, String word, int index) {
        if (index == word.length()) {
            if (node.isEndOfWord) {
                node.isEndOfWord = false;
                size--;
            }
            return node.next.isEmpty();
        }

        char c = word.charAt(index);
        if (!node.next.containsKey(c))
            return false;

        boolean shouldDeleteNode = delete(node.next.get(c), word, index + 1)
                && !node.isEndOfWord;  // 如果没有这个条件，则删除 cattle 后 cat 的 t 也会被删掉

        if (shouldDeleteNode) {
            node.next.remove(c);
            return node.next.isEmpty();
        }

        return false;
    }

    /*
     * 查操作
     * */
    public boolean contains(String word) {
        return contains(root, word, 0);
    }

    private boolean contains(Node node, String word, int index) {
        if (index == word.length())
            return node.isEndOfWord;

        char c = word.charAt(index);
        if (!node.next.containsKey(c))
            return false;
        return contains(node.next.get(c), word, index + 1);
    }

    public boolean contiansNR(String word) {  // 非递归实现
        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!curr.next.containsKey(c))
                return false;
            curr = curr.next.get(c);
        }

        return curr.isEndOfWord;
    }

    public boolean startWith(String prefix) {  // 查询 Trie 中是否有单词以 prefix 为前缀。实现逻辑与 contains 方法极为相像
        Node curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!curr.next.containsKey(c))
                return false;
            curr = curr.next.get(c);
        }
        return true;
    }

    public boolean search(String word) {  // 该方法除了具有 contains 的功能，还支持正则表达式中的"."，即可以匹配任何字母
        return search(root, word, 0);
    }

    private boolean search(Node node, String word, int index) {
        if (index == word.length())
            return node.isEndOfWord;

        char c = word.charAt(index);
        if (c != '.') {
            if (!node.next.containsKey(c))
                return false;
            return search(node.next.get(c), word, index + 1);
        } else {
            for (char key : node.next.keySet())
                if (search(node.next.get(key), word, index + 1))
                    return true;
            return false;
        }
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }
}
