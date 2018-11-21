package Map;

/*
 * - 这里使用 AVLTree 实现 Map
 * */

import java.util.ArrayList;
import java.util.List;

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

    public void inOrderTraverse(Node node, List<K> list) {
        if (node == null)
            return;
        inOrderTraverse(node.left, list);
        list.add(node.key);
        inOrderTraverse(node.right, list);
    }

    private int getBalanceFactor(Node node) {  // 计算平衡因子
        if (node == null)
            return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T3 = x.right;

        x.right = y;
        y.left = T3;

        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));

        return x;
    }

    private Node leftRotate(Node y) {
        Node x = y.right;
        Node T2 = x.left;

        x.left = y;
        y.right = T2;

        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));

        return x;
    }

    private Node maintainBalance(Node node) {  // add 或 remove 之后，路径上的所有节点都需要维护平衡
        // 1. 更新 height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // 2. 计算平衡因子
        int balanceFactor = getBalanceFactor(node);

        // 3. 恢复平衡
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

        return maintainBalance(node);
    }

    /*
     * 删操作
     */
    public V remove(K key) {
        Node node = getNode(root, key);
        if (node != null) {
            root = remove(root, key);
            return node.value;
        }
        return null;
    }

    private Node remove(Node node, K key) {
        if (node == null)
            return null;

        Node retNode;  // 执行完删除逻辑的节点不要马上返回，先用变量接住
        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);
            retNode = node;
        }
        else if (key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
            retNode = node;
        }
        else {
            if (node.left == null) {
                Node rightNode = node.right;
                node.right = null;
                size--;
                retNode = rightNode;
            }
            else if (node.right == null) {
                Node leftNode = node.left;
                node.left = null;
                size--;
                retNode = leftNode;
            }
            else {
                Node successor = getMin(node.right);
                successor.right = remove(node.right, successor.key);  // BST 中此处用的是 removeMin(node.right)，但 removeMin 方法中没有维护平衡，所以此处这样
                successor.left = node.left;
                node.left = node.right = null;
                retNode = successor;
            }
        }

        return retNode != null  // 删除节点之后有可能得到的是空节点（如删除叶子节点），此时就不需要维护平衡了
                ? maintainBalance(retNode)
                : null;
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

    /*
    * AVLTree 检测方法
    * */
    public Node getRoot() { return root; }

    public boolean isBST(Node node) {  // 判断该二叉树是否是一棵二分搜索树。利用 BST 的特性 ——— 对 BST 进行中序遍历时，所有元素是被顺序访问的
        List<K> keys = new ArrayList<>();
        inOrderTraverse(node, keys);  // 将树上的每一个节点的 key 放入 keys 中

        for (int i = 1; i < keys.size(); i++)
            if (keys.get(i - 1).compareTo(keys.get(i)) > 0)  // 如果不是升序，则说明不是 BST
                return false;
        return true;
    }

    public boolean isBalanced() {  // 判断是否是平衡二叉树
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null)
            return true;
        if (Math.abs(getBalanceFactor(node)) > 1)
            return false;
        return isBalanced(node.left) && isBalanced(node.right);
    }
}
