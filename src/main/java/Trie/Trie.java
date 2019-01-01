package Trie;

/*
* - Trie（字典树）：
*   1. 是一种多叉树
*   2. 通常只用于处理字符串
*   3. 复杂度是 O(w)，w 为待查询的字符串长度，而跟数据条数是无关的
*
* - 对于查询操作，使用平衡二叉树的复杂度是 O(logn)，已经很高效了，但是对于百万级（2^20）的数据量，平衡二叉树大约是20；
*   而使用 Trie 的话，查询的时间复杂度跟数据量无关，只跟要查询的字符串的长度 w 相关，即 O(w)，而大对数英语单词的
*   长度都在10以下，因此数据量越大，Trie 的性能优势相比平衡二叉树就越明显。
* - 影响 Tire 性能的因素：
*   1. Trie 中节点的 next 是使用哪种 map 实现，如果使用 java 的 TreeMap（底层是红黑树）则会比使用 HashMap 差一些。
*   2. 由于 Trie 消耗的空间比较大，所以给这些承载指针的空间（TreeMap，HashMap 或者数组）开辟内存也是一个额外的时间消耗。
*
* - 一个存有 dog, deer, cat, cattle, camel 的 Trie 长这样：
*              root
*            /     \
*           d       c
*          / |      \
*         o  e       a
*        /   |      / \
*       g*   e     m   t*
*            |     |    \
*            r*    e     t
*                  |      \
*                  l*      l
*                           \
*                            e*
* - 因为 Trie 是多叉树，其每个节点可能有多个子节点。如果是英文，则每个节点至少有26个子节点，而至多则有
*   26小写 + 26大写 + n个标点符号个子节点。
* - 要实现 Tire 需要先定义清楚每一个节点。Trie 的节点具有如下性质：
*   1. 可动态添加删除
*   2. 每个节点是一个映射，key 为字符、value 为子节点（节点可以不保存字符，实际上节点之间的边才是字符）
*   3. 每个节点维护一个 boolean 值，用于标记该节点对应的字母是否是一个单词的结尾（如 cat 在 camel、cattle 中，
*      因此 t 对应的节点需要标记为单词末尾）
*
* - Trie 也叫做"前缀树"，因为 Trie 可以非常方便地查看当前存储的所有单词中是否有某个前缀对应的单词。
*
* - Trie 最大的问题在于空间复杂度高 —— 两个字符之间的链接是通过 map 建立的（每个节点会有若干个指针），因此比普通
*   字符串的空间开销要大得多。为了解决这个问题，Trie 有一些变种，最常见的就是压缩字典树（Compressed Trie），
*   将没有分支的节点合并：
*             root
*           /     \
*          d       c
*         / |      \
*       og  eer     a
*                  / \
*               mel   t*      <- 注意这里 t 是 cat 的结尾，因此不能被合并到后面的 tle 里去
*                      \
*                      tle
* - 压缩字典树虽然在空间消耗上有所改进，但是添加操作会更复杂（有得就有失），因为有时候需要先将一个压缩的字符
*   串拆开，再往里添加。
* - 另一种改造方式是三分搜索树（Ternary Search Trie），同时也是一个经典数据结构。其形态如下：
*              d
*          /   |   \
*        a     k    z
*            / | \
*                 o
*               / | \
*
* - 每个节点有三个子节点，如果：
*   1. 待查找字符 == 当前节点中的字符，则选择中间路径
*   2. 待查找字符 < 节点中的字符，则选择左边路径
*   3. 待查找字符 > 节点中的字符，则选择右边路径
* - 中间分支存储的是在和该节点匹配成功的情况下的下一个字母，这点和 Trie 是一样的。
*   若匹配不成功，则去左边或者右边继续找这个字母。这点和 Trie 不同。在 Trie 中只可能匹配
*   成功，每下降一层，一定找的是一个新的字母。
* - 三分搜索树的时间复杂度也是和要搜索的字符串的长度成正比，但要比 Trie 慢一点（因为牺牲了时间换空间），而空间复杂度也得到了控制。
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

        if (!curr.isEndOfWord) {  // 该单词结尾没有被标记过（即该单词原先不存在于该 trie 中）
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
        for (int i = 0; i < word.length(); i++) {  // 或 for (Character c : word.toCharArray())
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
