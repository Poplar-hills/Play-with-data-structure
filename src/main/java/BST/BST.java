package BST;

/*
* Binary Search Tree 二叉查找树
*
*         28
*       /    \
*     16     30
*    /  \   /  \
*  13   22 29  42
*
* - 对 BST 的遍历分为前序、中序、后序3种遍历方法，其中：
*   - 前序遍历顺序是：当前节点->左子树->右子树。因此，树上节点的访问顺序是：28->16->13->22->30->29->42（上层->底层）。
*   - 中序遍历顺序是：左子树->当前节点->右子树。因此，树上节点的访问顺序是：13->16->22->28->29->30->42。
*   - 而因为一个节点的左子树都比该节点小，而右子树都比该节点大，因此中序遍历的结果是从小到大顺序排列的。
*   - 后序遍历顺序是：左子树->右子树->当前节点。因此，树上节点的访问顺序是：13->22->16->29->42->30->28（底层->上层）。
*   - 后序遍的一个应用是为 BST 释放内存，即对于一个节点需要先释放所有子节点的内存，再释放该节点内存，因此需要后续遍历。
*     因为 Java 是自动垃圾回收，所以不需要操心这个问题，但 C++ 就需要了。
* - 实际上在一次 BST 的遍历中，每个节点都有3次访问机会（前、中、后序）就看需要什么样的访问顺序了。
* */

import java.util.Stack;

public class BST<E extends Comparable> {
    Node root;
    int size;

    private class Node<E> {
        private E e;
        private Node left, right;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
        }
    }

    public BST() {
        root = null;
        size = 0;
    }

    /*
     * 增操作
     * */
    public void add(E e) {
        root = add(root, e);  // 插入之后的结果都是更新这棵树
    }

    private Node add(Node node, E e) {  // 因为二叉树具有递归性质，该方法用于将一个节点添加到某一个子树上
        // 递归的终止条件：插入元素的结果都是生成新节点、size + 1
        if (node == null) {  // 递归到底，没有子节点了
            size++;
            return new Node(e);
        }
        // 递归的最小重复单元：更新左子树或右子树
        if (e.compareTo(node.e) < 0)
            node.left = add(node.left, e);
        else if (e.compareTo(node.e) > 0)  // 注意 compareTo(...) == 0 的情况不处理，这个 BST 的实现不允许存在重复节点
            node.right = add(node.right, e);

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
        // 递归的终止条件
        if (node == null)  // 递归到底，没有子节点了
            return false;
        if (e.compareTo(node.e) == 0)  // 相当于 e.equals(node.e)
            return true;

        // 递归的最小重复单元
        return contains(e.compareTo(node.e) < 0 ? node.left : node.right, e);
    }

    /*
     * Traverse
     * */
    public void preOrderTraverse() {  // pre-order（前序），即在先访问节点，再访问左右子树
        preOrderTraverse(root);
    }

    private void preOrderTraverse(Node node) {
        if (node == null) return;
        System.out.println(node.e);  // 先访问节点
        preOrderTraverse(node.left);  // 再访问左右子树。如果是中序遍历就是把上面访问节点的语句放在这句下面
        preOrderTraverse(node.right);  // 如果是后序遍历就是把上面访问节点的语句放在这句下面
    }

    public void preOrderTraverseNR() {  // 前序遍历的非递归实现；实际应用很少（一般都用递归）；中序和后序的非递归实现更复杂
        Stack<Node> stack = new Stack<Node>();  // 用栈数据结构实现遍历
        if (root == null) return;
        stack.push(root);
        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            System.out.println(curr);

            if (curr.right != null)
                stack.push(curr.right);  // 因为栈是后入先出，所以要先压入右子树，再压入左子树，让左子树先出栈
            if (curr.left != null)
                stack.push(curr.left);
        }
    }

    public void inOrderTraverseNR() {  // 中序遍历的非递归实现
        Stack<Node> stack = new Stack<Node>();
        if (root == null) return;

        Node curr, parent;
        curr = root;
        stack.push(curr);
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // 一直往一个方向走，不断获取左子树，直到叶子节点
                stack.push(curr.left);
                curr = curr.left;
            }

            if (!stack.isEmpty()) {  // 当 curr == null，此时到达叶子节点，需要变换方向
                parent = stack.pop();  // 获取上一层节点
                System.out.println(curr);  // 访问节点
                curr = parent.right;
            }
        }
    }

    /*
    * Misc
    * */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        return toString(root,0, s).toString();
    }

    private StringBuilder toString(Node node, int depth, StringBuilder s) {
        if (node == null) {
            s.append(getDepthSign(depth) + "null\n");
            return s;
        } else {
            s.append(getDepthSign(depth) + node.e + "\n");
            toString(node.left, depth + 1, s);
            toString(node.right, depth + 1, s);
        }
        return s;
    }

    private String getDepthSign(int depth) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            s.append("--");
        }
        s.append(" ");
        return s.toString();
    }
}
