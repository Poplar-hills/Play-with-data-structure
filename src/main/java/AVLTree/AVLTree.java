package AVLTree;

/*
* - AVL tree 也是最早的自平衡二分搜索树。"AVL"是其发明人（俄国人）的名字首字母缩写。
* - 之前我们实现的 BST 的最大的问题是，当将一组数据顺序 add 到 BST 上时，会得到一棵形如链表的树，从而失去了 BST 的性能优势。
*   解决办法就是在 BST 上添加一些机制使得它在任何情况下都是一棵平衡二叉树，从而使得高度和节点数量之间的关系仍然是 O(logn)。
* - AVL tree 就是一种最经典的平衡二分搜索树（之前讲过的堆、线段树也是平衡二叉树，但不是平衡二分搜索树）。
*
* - 之前我们对平衡二叉树的定义是：对于任意叶子节点，其最大深度与最小深度的差不超过1，并且左右两个子树都是一棵平衡二叉树。
*   而 AVL tree 对"平衡二叉树"的定义稍微宽松一些：对于任意一个节点（不一定是叶子节点），左子树和右子树的高度差不超过1。例如：
*                   12                    12
*                 /    \                /    \
*               8      18             8      18
*              / \     /             / \     /
*            5   11   17           5   11   17
*           /                     / \
*         4                     4   7
*                              /
*                            2
*   - 左边的是平衡二叉树，符合 AVL tree 对平衡二叉树的定义，其高度和节点数量之间的关系仍然是 O(logn)。
*   - 右边的就不是了，在添加了节点2之后，节点8就成了不平衡节点（左子树高度 - 右子树高度 > 1）。
*
* - 为了实现平衡二叉树：
*   1. 在 BST 的基础上需要每一个节点记录自身所在高度；
*   2. 每个节点在知道其左右两个子节点的高度之后就可以计算该节点上的"平衡因子"（balance factor）。
*      计算方式为：max(左节点高度, 右节点高度) + 1。一旦某个平衡因子的绝对值 > 1，则整棵树就不再平衡。
*
* - 有了平衡因子之后就可以识别出哪个节点是"不平衡节点"，从而对其进行左旋/右旋变换，使其回归平衡。具体可分几种情况：
*   1. 如果新添加的节点在不平衡节点的左子树的左子树上，则需对不平衡节点右旋。
*   2. 如果新添加的节点在不平衡节点的右子树的右子树上，则需对不平衡节点左旋。
*   3. 如果新添加的节点在不平衡节点的左子树的右子树上，则需先对不平衡节点的左子树左旋，再对不平衡节点右旋。
*   4. 如果新添加的节点在不平衡节点的右子树的左子树上，则需先对不平衡节点的右子树右旋，再对不平衡节点左旋。
* - 旋转之后的树应该同时满足：1. 平衡二叉树的性质  2. 二分搜索树的性质。
*   分析过程 SEE: https://coding.imooc.com/lesson/207.html#mid=14349
* */

import java.util.ArrayList;
import java.util.List;

public class AVLTree<E extends Comparable<E>> {
    Node root;
    int size;

    private class Node {
        private E e;
        private Node left, right;
        private int height;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
            height = 1;  // 新添加的节点一定都是叶子节点，因此高度为1
        }
    }

    public AVLTree() {
        this.root = null;
        size = 0;
    }

    /*
     * 辅助方法
     * */
    private Node getNode(Node node, E e) {
        if (node == null)
            return null;
        if (e.compareTo(node.e) < 0)
            return getNode(node.left, e);
        else if (e.compareTo(node.e) > 0)
            return getNode(node.right, e);
        else
            return node;
    }

    private int getHeight(Node node) {  // 获得节点在树中的高度
        if (node == null)  // 设置这个方法的目的主要是处理 null 的情况
            return 0;
        return node.height;
    }

    public void inOrderTraverse(Node node, List<E> elements) {
        if (node == null)
            return;
        inOrderTraverse(node.left, elements);
        elements.add(node.e);
        inOrderTraverse(node.right, elements);
    }

    private int getBalanceFactor(Node node) {  // 计算平衡因子
        if (node == null)
            return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    /*
     *            y                     x
     *          /   \     对y右旋     /    \
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
     *     /  \      对y左旋     /    \
     *   T1    x    ------->   y      z
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

    private Node maintainBalance(Node node) {
        // 1. 更新 height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // 2. 计算平衡因子
        int balanceFactor = getBalanceFactor(node);

        // 3. 检查、恢复平衡
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
     * */
    public void add(E e) { root = add(root, e); }

    private Node add(Node node, E e) {
        if (node == null) {
            size++;
            return new Node(e);
        }

        if (e.compareTo(node.e) < 0)
            node.left = add(node.left, e);
        else if (e.compareTo(node.e) > 0)
            node.right = add(node.right, e);

        return maintainBalance(node);
    }

    /*
     * 删操作
     * */
    public void remove(E e) {
        root = remove(root, e);
    }

    private Node remove(Node node, E e) {
        if (node == null)
            return null;

        Node retNode;  // 该变量持有删除节点后所返回的节点
        if (e.compareTo(node.e) < 0) {
            node.left = remove(node.left, e);
            retNode = node;
        }
        else if (e.compareTo(node.e) > 0) {
            node.right = remove(node.right, e);
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
                successor.right = remove(node.right, successor.e);  // BST 中此处用的是 removeMin(node.right)，但 removeMin 方法中没有维护平衡，所以此处递归调用 remove 删除节点并维护平衡。
                successor.left = node.left;
                node.left = node.right = null;
                retNode = successor;
            }
        }

        return retNode != null
                ? maintainBalance(retNode)
                : null;  // 当删除叶子节点后返回的就是 null，此时可以直接返回上一层对上一层的节点进行平衡检查和恢复
    }

    /*
     * 查操作
     * */
    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    public boolean contains(E e) {
        return contains(root, e);
    }

    private boolean contains(Node node, E e) {
        if (node == null)
            return false;
        if (e.compareTo(node.e) == 0)
            return true;
        return contains(e.compareTo(node.e) < 0 ? node.left : node.right, e);
    }

    public E getMin() {
        if (size == 0)
            throw new IllegalArgumentException("getMin failed. Empty tree");
        return getMin(root).e;
    }

    private Node getMin(Node node) {
        if (node.left == null)
            return node;
        return getMin(node.left);
    }

    /*
    * AVLTree 检测方法
    * */
    public Node getRoot() { return root; }

    public boolean isBST(Node node) {  // 判断该二叉树是否是一棵二分搜索树。利用 BST 的特性 ——— 对 BST 进行中序遍历时，所有元素是被顺序访问的
        List<E> elements = new ArrayList<>();
        inOrderTraverse(node, elements);  // 将树上的每一个节点的 key 放入 keys 中

        for (int i = 1; i < elements.size(); i++)
            if (elements.get(i - 1).compareTo(elements.get(i)) > 0)  // 如果不是升序，则说明不是 BST
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
