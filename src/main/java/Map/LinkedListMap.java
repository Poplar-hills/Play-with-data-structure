package Map;

import javafx.util.Pair;

public class LinkedListMap<K, V> implements Map<K, V> {
    private Node dummyHead;
    private int size;

    private class Node {
        public K key;
        public V value;
        public Node next;

        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node(K key, V value) { this(key, value,null); }

        public Node() { this(null, null,null); }

        @Override
        public String toString() { return key.toString() + ": " + value.toString(); }
    }

    public LinkedListMap() {
        dummyHead = new Node();
        size = 0;
    }

    /*
     * 辅助方法
     */
    private Node getNode(K key) {
        for (Node curr = dummyHead.next; curr != null; curr = curr.next) {
            if (curr.key.equals(key))
                return curr;
        }
        return null;
    }

    /*
     * 增操作
     */
    public void add(K key, V value) {
        Node node = getNode(key);
        if (node == null) {  // 无则添加
            dummyHead.next = new Node(key, value, dummyHead.next);
            size++;
        } else
            node.value = value;  // 有则更新
    }

    /*
     * 删操作
     */
    public V remove(K key) {
        Pair<Node, V> res = remove(dummyHead.next, key);
        return res.getValue();
    }

    private Pair<Node, V> remove(Node node, K key) {
        if (node == null)
            return null;
        if (node.key.equals(key)) {
            Node next = node.next;
            node.next = null;
            size--;
            return new Pair<Node, V>(next, node.value);
        }
        Pair<Node, V> res = remove(node.next, key);
        node.next = res.getKey();
        return new Pair<Node, V>(node, res.getValue());
    }

    /*
     * 改操作
     */
    public void set(K key, V value) {
        Node node = getNode(key);
        if (node == null)  // 当 key 不存在时报错，因为用户在更新一个 key 的 value 时，一定是比较肯定 key 是存在的，所以如果不存在，应当警告用户
            throw new IllegalArgumentException("set failed. " + key + " doesn't exist.");
        node.value = value;
    }

    /*
     * 查操作
     */
    public V get(K key) {
        Node node = getNode(key);
        return node != null ? node.value : null;
    }

    public boolean contains(K key) { return getNode(key) != null; }

    public boolean isEmpty() { return size == 0; }

    public int getSize() { return size; }

    /*
    * Misc
    * */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Size: " + getSize() + " { ");
        for (Node curr = dummyHead.next; curr != null; curr = curr.next) {
            s.append(curr.toString());
            if (curr.next != null)
                s.append(", ");
        }
        s.append(" }");
        return s.toString();
    }
}
