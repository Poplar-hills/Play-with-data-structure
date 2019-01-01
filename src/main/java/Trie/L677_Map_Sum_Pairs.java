package Trie;

import java.util.HashMap;

/*
* 该题中，Trie 被当做一个 map 使用（我们之前实现的普通 Trie 实际上可以作为 set 使用）
* */

public class L677_Map_Sum_Pairs {
    private Node root;
    private int size;

    private class Node {
        public int value;  // 记录节点值
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
        if (index >= prefix.length()) {  // 直至将 prefix 的所有字符遍历完之后再开始计算又有后续字符的 value 的和
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

    public int sumNR(String prefix) {  // 非递归实现
        Node curr = root;
        for (char c : prefix.toCharArray()) {
            if (!curr.next.containsKey(c))  // Trie 中没有 prefix 则返回 0
                return 0;
            curr = curr.next.get(c);
        }
        return sumOfNode(curr);  // 此时已经到达 prefix 的最后一个字符，可以开始计算其后续所有节点的 value 和了
    }

    private int sumOfNode(Node node) {
        int result = node.value;

        for (char c : node.next.keySet())
            result += sumOfNode(node.next.get(c));  // 递归计算后续节点的 value 和

        return result;
    }
}
