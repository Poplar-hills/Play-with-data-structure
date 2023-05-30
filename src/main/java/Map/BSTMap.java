package Map;

import javafx.util.Pair;

/*
* - 这里使用 BST 实现 Map。
* - 与 Set 一样，Map 也分为：
*   1. 有序映射（如 BSTMap）：键具有顺序性
*   2. 无序映射两种（如 HashMap）：键没有顺序性
*   3. 多重映射（如 Guava 中的 MultiMap）：键是可以重复的，重复的键对应的值会被集合起来，其结构类似 Map<K, List<V>> 或 Map<K, Set<V>>
*
* - 除了 LinkedList 和 BST 以外，Set 也可以作为 Map 的底层实现：只需要将 Set 中的元素定义为键值对，并且只比较键是否重复，不用管值
* - 而反过来，Map 也可以作为 Set 的底层实现：只需要将键值对中的值置空即可（只有键没有值的 Map 就是 Set）
* */

public class BSTMap<K extends Comparable<K>, V> implements Map<K, V> {
    private Node root;
    private int size;

    private class Node {
        public K key;
        public V value;
        public Node left, right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
        }

        @Override
        public String toString() { return key.toString() + ": " + value.toString(); }
    }

    public BSTMap() {
        root = null;
        size = 0;
    }

    /*
     * 辅助方法
     */
    private Node getNode(Node node, K key) {
        if (node == null)
            return null;
        if (key.compareTo(node.key) < 0)
            return getNode(node.left, key);
        else if (key.compareTo(node.key) > 0)
            return getNode(node.right, key);
        else
            return node;
    }

    /*
     * 增操作
     */
    public void add(K key, V value) { root = add(root, key, value); }

    private Node add(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key.compareTo(node.key) < 0)
            node.left = add(node.left, key, value);
        else if (key.compareTo(node.key) > 0)
            node.right = add(node.right, key, value);
        else
            node.value = value;  // 当 key 相同时，不添加 key，只更新 value

        return node;
    }

    /*
     * 删操作
     */
    public V remove(K key) {
        Pair<Node, V> res = remove(root, key);
        root = res.getKey();
        return res.getValue();
    }

    private Pair<Node, V> remove(Node node, K key) {  // 这种返回 node/value pair 的 remove 操作比视频中的先 getNode，再 remove 的方式要高效，因为只递归一遍
        if (node == null)  // 从根节点到叶子节点的整个路径上都没找到 e
            return null;

        if (key.compareTo(node.key) < 0) {
            Pair<Node, V> res = remove(node.left, key);
            node.left = res.getKey();
            return new Pair<Node, V>(node, res.getValue());
        } else if (key.compareTo(node.key) > 0) {
            Pair<Node, V> res = remove(node.right, key);
            node.right = res.getKey();
            return new Pair<Node, V>(node, res.getValue());
        } else {  // 递归的终止条件：key.compareTo(node.key) == 0 即 key.equals(node.key)，即该节点就是要删除的节点
            if (node.left == null) {  // 如果待删除节点只有右子树，或左右都没有
                Node rightNode = node.right;
                node.right = null;
                size--;
                return new Pair<Node, V>(rightNode, node.value);
            }

            if (node.right == null) {  // 如果待删除节点只有左子树，或左右都没有
                Node leftNode = node.left;
                node.left = null;
                size--;
                return new Pair<Node, V>(leftNode, node.value);
            }

            // 如果待删除节点左右子数都存在，则使用 Hibbard Deletion 方法：
            // 1. 找到后继节点（比待删除节点大的最小节点，即待删除节点右子树的左下角节点。其实找前驱节点也可以，即比待删除节点小的最大节点）
            // 2. 用这个节点取代待删除节点的位置
            // 3. 删除待删除节点
            Node successor = getMin(node.right);
            successor.right = removeMin(node.right);  // removeMin 里发生了一次 size--，因此后面不用再减了。另外，此处顺序很重要，要先给 successor.right 赋值
            successor.left = node.left;  // 再给 successor.left 赋值，因为 removeMin 中要找到 node.right 的左下角元素，当递归到 node 就是 successor 的时候，如果 node.left 已经被赋了新值，则就形成了循环引用
            node.left = node.right = null;
            return new Pair<Node, V>(successor, node.value);
        }
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            Node rightNode = node.right;
            node.right = null;
            size--;
            return rightNode;
        }
        node.left = removeMin(node.left);
        return node;
    }

    /*
     * 改操作
     */
    public void set(K key, V value) {
        Node node = getNode(root, key);
        if (node == null)  // 当 key 不存在时报错，因为用户在更新一个 key 的 value 时，一定是比较肯定 key 是存在的，所以如果不存在，应当警告用户
            throw new IllegalArgumentException("set failed. " + key + " doesn't exist.");
        node.value = value;
    }

    /*
     * 查操作
     */
    public V get(K key) {
        Node node = getNode(root, key);
        return node != null ? node.value : null;
    }

    private Node getMin(Node node) {
        return node.left == null ? node : getMin(node.left);
    }

    public boolean contains(K key) {
        return getNode(root, key) != null;
    }

    public boolean isEmpty() { return size == 0; }

    public int getSize() { return size; }

    /*
     * Misc
     * */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Size: " + getSize() + " { ");
        toString(root, s);
        s.append(" }");
        return s.toString();
    }

    private void toString(Node node, StringBuilder s) {
        if (node == null)
            return;

        s.append(node.toString());

        if (node.left != null) {
            s.append(", ");
            toString(node.left, s);
        }
        if (node.right != null) {
            s.append(", ");
            toString(node.right, s);
        }
    }
}
