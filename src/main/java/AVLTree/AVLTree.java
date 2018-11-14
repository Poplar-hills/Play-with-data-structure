package AVLTree;

/*
* - 之前我们实现的 BST 的最大的问题是，当将一组数据顺序 add 到 BST 上时，会得到一棵形如链表的树，从而失去了 BST 的性能优势。
*   解决办法就是在 BST 上添加一些机制使得它在任何情况下都是一棵平衡二叉树，从而使得高度和节点数量之间的关系仍然是 O(logn)。
* - AVL tree 就是一种最为经典的平衡二分搜索树（之前讲过的堆、线段树也是平衡二叉树，但不是平衡二分搜索树）。
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
*   左边的是平衡二叉树，符合 AVL tree 对平衡二叉树的定义，其高度和节点数量之间的关系仍然是 O(logn)，而右边的就不是了。
* - 为了实现平衡二叉树，需要每一个节点记录自身所在高度；而每个节点在知道其左右两个子节点的高度之后就可以计算该节点上的
*   "平衡因子"（balance factor），计算方法是：max(左节点高度, 右节点高度) + 1。一旦某个平衡因子的绝对值 > 1，则整棵
*   树就不再平衡。
* - 有了平衡因子之后就可以识别出哪个节点是"不平衡节点"，从而对其进行左旋/右旋变换，使其回归平衡。具体可分几种情况：
*   1. 树向一侧偏斜
*     a. 如果新添加的节点在不平衡节点的左子树的左子树上，则采用右旋恢复平衡。
*     b. 如果新添加的节点在不平衡节点的右子树的右子树上，则采用左旋恢复平衡。
*   2.
* - 旋转之后的树应该同时满足：1. 平衡二叉树的性质  2. 二分搜索树的性质。分析过程 SEE: https://coding.imooc.com/lesson/207.html#mid=14349
* */

public class AVLTree<E extends Comparable<E>> {
    Node root;
    int size;

    private class Node {
        private E e;
        private Node left, right;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
        }
    }

    public AVLTree() {
        this.root = null;
        size = 0;
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

        return node;
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

        if (e.compareTo(node.e) < 0)
            node.left = remove(node.left, e);
        if (e.compareTo(node.e) > 0)
            node.right = remove(node.right, e);
        else {
            if (node.left == null) {
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }

            if (node.right == null) {
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            }

            Node successor = getMin(node.right);
            successor.right = remove(node.right, successor.e);
            successor.left = node.left;
            node.left = node.right = null;
            return successor;
        }

        return node;
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
}
