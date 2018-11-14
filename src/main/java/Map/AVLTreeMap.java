package Map;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/*
 * - 这里使用 AVLTree 实现 Map
 * */

public class AVLTreeMap<K extends Comparable<K>, V> implements Map<K, V> {
    private Node root;
    private int size;

    private class Node {
        public K key;
        public V value;
        public Node left, right;
        public int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            height = 1;
        }

        @Override
        public String toString() { return key.toString() + ": " + value.toString(); }
    }

    public AVLTreeMap() {
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

    private int getHeight(Node node) {  // 获得节点在书中的高度
        if (node == null)  // 设置这个方法的目的主要是处理 null 的情况
            return 0;
        return node.height;
    }

    private int getBalanceFactor(Node node) {  // 计算平衡因子
        if (node == null)
            return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    private boolean isBST(Node node) {  // 判断该二叉树是否是一棵二分搜索树。利用 BST 的特性 ——— 对 BST 进行中序遍历时，所有元素是被顺序访问的
        List<K> keys = new ArrayList<>();
        inOrderTraverse(node, keys);  // 将树上的每一个节点的 key 放入 keys 中

        for (int i = 1; i < keys.size(); i++)
            if (keys.get(i - 1).compareTo(keys.get(i)) > 0)  // 如果不是升序，则说明不是 BST
                return false;
        return true;
    }

    private void inOrderTraverse(Node node, List<K> keys) {
        if (node == null)
            return;
        inOrderTraverse(node.left, keys);
        keys.add(node.key);
        inOrderTraverse(node.right, keys);
    }

    private boolean isBalanced() {  // 判断是否是平衡二叉树
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null)
            return true;
        if (Math.abs(getBalanceFactor(node)) > 1)
            return false;
        return isBalanced(node.left) && isBalanced(node.right);
    }

    /*
    *            y                     x
    *          /   \      右旋       /    \
    *         x    T4   ------>    z      y
    *       / \                  / \     / \
    *      z  T3                T1 T2   T3 T4
    *    /  \
    *  T1   T2
    *
    *  旋转后满足：
    *    1. 平衡二叉树的性质：分析过程 SEE: https://coding.imooc.com/lesson/207.html#mid=14349  14'50''
    *    2. 二分搜索树的性质：T1 < z < T2 < x < T3 < y < T4（旋转前后都满足）
    * */
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T3 = x.right;

        // 左旋转
        x.right = y;
        y.left = T3;

        // 维护 x, y 的 height（其它节点高度不变，因为只有 x, y 的子树发生了变动）
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));  // 需要先维护 y 的高度，因为 y 已经变成 x 的子节点了，有了 y 的高度才能计算 x 的高度
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));

        return x;  // 返回旋转后的根节点
    }

    /*
     *       y                     x
     *     /  \       左旋       /    \
     *   T1    x    ------>    y      z
     *       /  \            / \     / \
     *     T2    z          T1 T2   T3 T4
     *         /  \
     *        T3  T4
     * */
    private Node leftRotate(Node y) {
        Node x = y.right;
        Node T2 = x.left;

        x.left = y;
        y.right = T2;

        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));

        return x;
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
            node.value = value;

        // 更新 height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // 计算平衡因子
        int balanceFactor = getBalanceFactor(node);

        // 恢复平衡
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0)  // 当新添加结点在不平衡节点左子树的左子树上，即 LL 情况
            return rightRotate(node);  // 返回保持平衡的 BST
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0)  // 当新添加结点在不平衡节点右子树的右子树上，即 RR 情况
            return leftRotate(node);
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {  // 当新添加结点在不平衡节点左子树的右子树上，即 LR 情况
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) { // 新添加结点在不平衡节点右子树的左子树上，即 RL 情况
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

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

    private Pair<Node, V> remove(Node node, K key) {
        if (node == null)
            return null;

        if (key.compareTo(node.key) < 0) {
            Pair<Node, V> res = remove(node.left, key);
            node.left = res.getKey();
            return new Pair<Node, V>(node, res.getValue());
        } else if (key.compareTo(node.key) > 0) {
            Pair<Node, V> res = remove(node.right, key);
            node.right = res.getKey();
            return new Pair<Node, V>(node, res.getValue());
        } else {
            if (node.left == null) {
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

            Node successor = getMin(node.right);
            successor.right = removeMin(node.right);
            successor.left = node.left;
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
