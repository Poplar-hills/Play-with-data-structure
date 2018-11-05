package Trie;

import java.util.HashMap;

public class L677_Map_Sum_Pairs {
    private Node root;
    private int size;

    private class Node {
        public int value;
        public HashMap<Character, Node> next;

        public Node(int value) {  // 本题中的 node 不再需要 isEndOfWord 字段，因为可以用 value 代替
            this.value = value;
            this.next = new HashMap<>();
        }

        public Node() { this(0); }
    }

    public L677_Map_Sum_Pairs() {
        root = new Node();
        size = 0;
    }

    public void insert(String key, int val) {  // 和 Trie 的 add 方法几乎完全一样
        insert(root, key, val, 0);
    }

    private void insert(Node node, String key, int val, int index) {
        if (index == key.length()) {
            node.value = val;
            size++;
            return;
        }
        char c = key.charAt(index);
        if (!node.next.containsKey(c))
            node.next.put(c, new Node());
        insert(node.next.get(c), key, val, index + 1);
    }

    public int sum(String prefix) {
        return sum(root, prefix, 0);
    }

    private int sum(Node node, String prefix, int index) {
        if (index >= prefix.length()) {  // 直至将 prefix 的所有字符遍历完之后再开始计算 value 的和
            int result = node.value;
            for (char c : node.next.keySet())
                result += sum(node.next.get(c), prefix, index + 1);
            return result;
        }

        char c = prefix.charAt(index);
        if (!node.next.containsKey(c))
            return 0;
        return sum(node.next.get(c), prefix, index + 1);
    }
}
