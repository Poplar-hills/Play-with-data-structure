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
* - 3 Types of Binary Tree
*   1. 满二叉树（full binary tree）：树中除了叶子节点，每个节点都有两个子节点
*   2. 完全二叉树（complete binary tree）：在满足满二叉树的性质后，最后一层的叶子节点均需在最左边
*   3. 完美二叉树（perfect  binary tree）：满足完全二叉树性质，树的叶子节点均在最后一层（也就是形成了一个完美的三角形）
*   - 满二叉树、完全二叉树、完美二叉树的定义是越来越严格的（可视化解释 SEE：https://www.geeksforgeeks.org/binary-tree-set-3-types-of-binary-tree）
*
* - 对 BST 的遍历分为前序、中序、后序3种遍历方法，其中：
*   - 前序遍历顺序是：当前节点->左子树->右子树。因此，树上节点的访问顺序是：28->16->13->22->30->29->42（上层->底层）。
*   - 中序遍历顺序是：左子树->当前节点->右子树。因此，树上节点的访问顺序是：13->16->22->28->29->30->42。
*   - 而因为一个节点的左子树都比该节点小，而右子树都比该节点大，因此中序遍历的结果是从小到大顺序排列的。
*   - 后序遍历顺序是：左子树->右子树->当前节点。因此，树上节点的访问顺序是：13->22->16->29->42->30->28（底层->上层）。
*   - 后序遍的一个应用是为 BST 释放内存，即对于一个节点需要先释放所有子节点的内存，再释放该节点内存，因此需要后续遍历。
*     因为 Java 是自动垃圾回收，所以不需要操心这个问题，但 C++ 就需要了。
* - 实际上在一次 BST 的遍历中，每个节点都有3次访问机会（前、中、后序）就看需要什么样的访问顺序了。
*
* - 除了这里实现的 BST 方法外，常见的还有 floor、ceil、rank、select 等，实现 SEE：https://coding.imooc.com/learn/questiondetail/63002.html
*
* - 对于树的遍历算法，无论是前中后序遍历，时间复杂度都是 O(n) 的，n 是树中节点个数。因为每一个节点在递归的过程中，只访问了一次。
* - 空间复杂度，通常说是 O(h) 的，h 是树的最大高度。这是因为在递归的过程中，每向下递归一层，就需要占用一定的系统栈空间。最多占
*   用 h 的空间。在这里要注意，我们说空间复杂度，通常是指完成算法所用的辅助空间的复杂度，而不用管算法前置的空间复杂度。比如在树
*   的遍历算法中，整棵树肯定要占 O(n) 的空间，n 是树中节点的个数，这部分空间是“固定成本”，即肯定存在的，所以不讨论它。
* - 对于平衡二叉树，我们会说其空间复杂度是 O(logn) 的，这是因为平衡二叉树的高度 h 就是 O(logn) 级别的。但是对于一般的树，严谨起见，
*   还是要说 O(h) 的。这个 h 可能是 logn（最好情况），也可能是 n（最坏情况）
* */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BST<E extends Comparable<E>> {  // 可比较的泛型
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

    public BST() {
        root = null;
        size = 0;
    }

    /*
     * 增操作
     * */
    public void add(E e) { root = add(root, e); }  // 插入之后的结果都是更新这棵树

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

        return node;  // 每次递归返回的都是子树更新后的根节点
    }

    /*
    * 删操作
    * */
    public E removeMin() {  // 从 BST 中删除值最小的节点
        E min = getMin();  // 找到最小值
        root = removeMin(root);  // 从树上删除最小值节点和上一步的找到最小值实际上是分离的操作
        return min;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {  // left == null 的 node 即为最小值节点
            Node rightNode = node.right;  // 可能有右子树，也可能没有，但可以统一操作
            node.right = null;
            size--;
            return rightNode;  // 将右子树返回给上一层节点，作为其左子树
        }
        node.left = removeMin(node.left);  // 返回节点作为左子树
        return node;  // 每次递归返回的都是子树更新后的根节点
    }

    public E removeMax() {
        E min = getMax();
        root = removeMax(root);
        return min;
    }

    private Node removeMax(Node node) {
        if (node.right == null) {
            Node leftNode = node.left;
            node.left = null;
            size--;
            return leftNode;
        }
        node.right = removeMax(node.right);
        return node;
    }

    public void remove(E e) {  // 从 BST 中删除任意一个节点
        root = remove(root, e);
    }

    private Node remove(Node node, E e) {
        if (node == null)  // 从根节点到叶子节点的整个路径上都没找到 e
            return null;

        if (e.compareTo(node.e) < 0) {
            node.left = remove(node.left, e);
            return node;
        } else if (e.compareTo(node.e) > 0) {
            node.right = remove(node.right, e);
            return node;
        } else {  // 递归的终止条件：e.compareTo(node.e) == 0 即 e.equals(node.e)，即该节点就是要删除的节点
            if (node.left == null) {  // 如果待删除节点只有右子树，或左右都没有
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }

            if (node.right == null) {  // 如果待删除节点只有左子树，或左右都没有
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            }

            // 如果待删除节点左右子数都存在，则使用 Hibbard Deletion 方法：
            // 1. 找到后继节点（比待删除节点大的最小节点，即待删除节点右子树的左下角节点。其实找前驱节点也可以，即比待删除节点小的最大节点）
            // 2. 用这个节点取代待删除节点的位置
            // 3. 删除待删除节点
            Node successor = getMin(node.right);
            successor.right = removeMin(node.right);  // removeMin 里发生了一次 size--，因此后面不用再减了。另外，此处顺序很重要，要先给 successor.right 赋值 SEE: https://coding.imooc.com/learn/questiondetail/84029.html
            successor.left = node.left;  // 再给 successor.left 赋值，因为 removeMin 中要找到 node.right 的左下角元素，当递归到 node 就是 successor 的时候，如果 node.left 已经被赋了新值，则就形成了循环引用
            node.left = node.right = null;
            return successor;
        }
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

    public E getMin() {  // 获取 BST 的最小值。因为每个节点的左孩子都比该节点小，因此整棵 BST 的最小值就在左下角的叶子节点上
        if (size == 0)
            throw new IllegalArgumentException("getMin failed. Empty tree");
        return getMin(root).e;
    }

    private Node getMin(Node node) {
        if (node.left == null)  // 终止条件是向左走到头，不再有左孩子的节点
            return node;
        return getMin(node.left);
    }

    public E getMax() {
        if (size == 0)
            throw new IllegalArgumentException("getMax failed. Empty tree");
        return getMax(root).e;
    }

    private Node getMax(Node node) {
        if (node.right == null)
            return node;
        return getMax(node.right);
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

    public void levelOrderTraverse() {
        if (root == null) return;
        System.out.println(root.e);
        levelOrderTraverse(root);
    }

    private void levelOrderTraverse(Node curr) {
        if (curr.left != null)
            System.out.println(curr.left.e);
        if (curr.right != null)
            System.out.println(curr.right.e);
        if (curr.left != null)
            levelOrderTraverse(curr.left);
        if (curr.right != null)
            levelOrderTraverse(curr.right);
    }

    public void levelOrderTraverseNR() {  // 层序遍历（广度优先遍历）
        Queue<Node> queue = new LinkedList<Node>();  // 使用链表实现的队列作为遍历时使用的数据结构
        queue.add(root);
        while (!queue.isEmpty()) {
            Node curr = queue.remove();
            System.out.println(curr.e);

            if (curr.left != null)
                queue.add(curr.left);
            if (curr.right != null)
                queue.add(curr.right);
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
