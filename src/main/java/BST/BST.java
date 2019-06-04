package BST;

/*
* Binary Search Tree 二叉查找树
*
* - Why?
*   - 使用二分搜索树通常是为了实现查找表（或叫字典），即键值对组合。
*   - 而查找表有多种实现：
*                     查找元素      插入元素      删除元素
*        普通数组       O(n)          O(n)         O(n)
*        顺序数组      O(logn)        O(n)         O(n)    -> 使用二分查找可以以 O(logn) 的复杂度进行查找
*        二分搜索树    O(logn)       O(logn)      O(logn)
*   - 可见二分搜索树实现的查找表效率是最高的，同时还具有顺序性（这是哈希表实现的查找表不具备的）。
*
* - 3 Types of Binary Tree
*   1. 满二叉树（full/strict binary tree）：树中除了叶子节点，每个节点都有两个子节点（"满"意味着"子节点已满"）。
*   2. 完全二叉树（complete binary tree）：叶子节点只在最后两层，且最后一层的叶子节点均在最左边（"完全"意味着每次添加节点都是在"补完"这棵树）。
*   3. 完美二叉树（perfect binary tree）：满足完全二叉树、满二叉树性质，且叶子节点均在最后一层（即形成了一个完美三角形）。
*
*              28                     28                     28
*            /    \                 /    \                 /    \
*          16     30              16      30             16     30
*         /  \                  /   \    /  \           /  \   /  \
*       13   22               13    22  29  34        13   22 29  42
*           /  \             /  \  /
*         18   25           8  15 20
*          满二叉树                完全二叉树              完美二叉树
*
*   - 完全二叉树不一定是满二叉树（不一定每个节点都有两个子节点）；
*   - 完美二叉树即是完全二叉树，也是满二叉树。
*
* - 树的平衡性：
*   - 定义：
*     - 堆、线段树中对"平衡"的定义：树中任意两个叶子节点的高度差不超过1。
*     - AVL 树中对"平衡"的定义：对于树中任何一个节点，其左右两个子树的高度差不超过1（这个定义比堆、线段树中的定义松一些）。
*   - 举例：
*     - 堆、AVL树、2-3树、红黑树、B树都是平衡树，而且都是自平衡树（添加节点之后仍然维持平衡）；线段树也是平衡树，但不是自平衡树（没有 add 方法）。
*     - 完全二叉树、完美二叉树一定是平衡二叉树。
*   - 意义：
*     - 如果说一棵树是“平衡”的，就意味着它的高度和节点数量成 log(n) 的关系。
*     - 更进一步来说，"平衡"是一个从操作复杂度角度定义的概念，即用来判断一棵树的操作复杂度是否是 O(logn)。例如说"堆是一种平衡树”，
*       实际上就意味着堆的各种操作（insert、extract）的复杂度都是 O(logn) 级别的”。
*
* - BST（二分搜索树）满足3个条件：
*   1. 是一种二叉树
*   2. 其上的每个节点的值：
*     a. 都大于其左子树上的任意节点的值
*     b. 都小于其右子树上的任意节点的值
*   3. 每个节点上存储的值必须具有可比较性
*
* - 遍历
*   - 对 BST 的深度优先遍历又分为前序、中序、后序3种遍历方法，其中：
*     - 前序：当前节点->左子树->右子树。树上节点的访问顺序是：28->16->13->22->30->29->42（上层->底层）。
*     - 中序：左子树->当前节点->右子树。树上节点的访问顺序是：13->16->22->28->29->30->42（因为每个节点的左子树都比该节点小，而右子树
*       都比该节点大，因此中序遍历的结果是从小到大顺序排列的）。
*     - 后序：左子树->右子树->当前节点。树上节点的访问顺序是：13->22->16->29->42->30->28（底层->上层）（后序遍的一个应用是为 BST 释
*       放内存，即对于一个节点需要先释放所有子节点的内存，再释放该节点内存，因此需要后续遍历。因为 Java 是自动垃圾回收，所以不需要操心
*       这个问题，但 C++ 就需要了）。
*     - 实际上在一次 BST 的遍历中，每个节点都有3次访问机会（前、中、后序）就看需要什么样的访问顺序了。
*   - 对 BST 的广度优先遍历（也叫层序遍历）一般要使用非递归，并要接住队列实现。
*   - 对于树的遍历，无论是前中后序遍历，时间复杂度都是 O(n)，其中 n 是树中节点个数。
*
* - BST 还具有另一个重要性质 —— 顺序性，即 BST 中存储元素使隐含顺序的（中序遍历的结果是从小到大排列的），因此
*   1. 基于 BST 实现的 TreeSet、TreeMap 都是具有顺序性的，而 HashSet、HashMap 则没有顺序性。
*   2. 很容易实现其他一些有用的方法：
*     - getSuccessor / getPredecessor：给定一个值，找到其前驱/后继。
*     - floor / ceil：给定一个值，找到比它小的最大值/比它大的最小值。
*     - rank / select：给定一个值，找到其在 BST 上的排名 / 给定一个排名，找到它在 BST 上的值；实现 rank, select 的最好方式是在每个节
*       点中维护一个 size 字段，记录以该节点为根的 BST 中共有多少个元素。
*     - 除了在每个节点中维护 size 字段以外，还可以维护 depth、count 等字段来实现其他常用功能（比如 count 字段使得 BST 能支持重复元素）。
*     - 参考实现 SEE：https://coding.imooc.com/learn/questiondetail/63002.html（ceil 和 floor 的实现有问题）。
*
* - BST 的时间复杂度分析：SEE: Set - PerformanceTest
*
* - BST 的空间复杂度：通常说是 O(h) 的，h 是树的最大高度。这是因为在递归的过程中，每向下递归一层，就需要占用一定的系统栈空间。最多占
*   用 h 的空间。在这里要注意，我们说空间复杂度，通常是指完成算法所用的辅助空间的复杂度，而不用管算法前置的空间复杂度。比如在树
*   的遍历算法中，整棵树肯定要占 O(n) 的空间，n 是树中节点的个数，这部分空间是“固定成本”，即肯定存在的，所以不讨论它。
* - 对于平衡二叉树，我们会说其空间复杂度是 O(logn) 的，这是因为平衡二叉树的高度 h 就是 O(logn) 级别的。但是对于一般的树，严谨起见，
*   还是要说 O(h) 的。这个 h 可能是 logn（最好情况），也可能是 n（最坏情况）
* */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

public class BST<E extends Comparable<E>> {  // 可比较的泛型
    private Node root;
    private int size;

    private class Node {
        private E e;
        private Node left, right;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
        }

        @Override
        public String toString() { return e.toString(); }
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
        if (root == null)
            throw new IllegalArgumentException("removeMin failed");
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
        if (root == null)
            throw new IllegalArgumentException("removeMax failed");
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
            successor.right = removeMin(node.right);  // removeMin 里发生了一次 size--，因此后面不用再减了。另外，此处顺序很重要，要先 removeMin（SEE: https://coding.imooc.com/learn/questiondetail/84029.html）
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

    public E search(E e) {  // 在 BST 中查找 key 所对应的 value（与 contains 的实现非常相似）
        return search(root, e);
    }  // 和 contains 的实现相差无几

    private E search(Node node, E e) {
        if (node == null)
            return null;
        if (e.compareTo(node.e) < 0)
            return search(node.left, e);
        if (e.compareTo(node.e) > 0)
            return search(node.right, e);
        return node.e;
    }

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
        return node.left == null ? node : getMin(node.left);
    }

    public E getMax() {
        if (size == 0)
            throw new IllegalArgumentException("getMax failed. Empty tree");
        return getMax(root).e;
    }

    private Node getMax(Node node) {
        return node.right == null ? node : getMax(node.right);
    }


    public E floor(E e) {  // 从 BST 上找出比给定值小的最大值（很好的练习，自己实现一下 ceil，思路类似）
        if (size == 0 || e.compareTo(getMin(root).e) < 0)  // 如果不存在 e 的 floor 值（树为空或 e 比树中的最小值还小）
            return null;
        return floor(root, e).e;
    }

    private Node floor(Node node, E e) {
        if (node == null)
            return null;
        if (e.compareTo(node.e) == 0)  // 如果 e == node.e，则该 node 就是 e 的 floor 节点
            return node;
        if (e.compareTo(node.e) < 0)  // 如果 e < node.e，则 e 的 floor 节点一定在 node 的左子树中（因为 floor 一定小于 e）
            return floor(node.left, e);
        // 如果 e > node.e，则 node 可能是 e 的 floor 节点，也可能不是，需要尝试在 node 的右子树中寻找。
        // 因为右子树中的节点一定都 > node，因此如果其中有 < e 的节点就一定是 floor。
        Node potentialFloor = floor(node.right, e);
        return potentialFloor != null
                ? potentialFloor
                : node;
    }

    /*
     * Traverse
     * */
    public void preorderTraverse(Consumer handler) {  // pre-order（前序），即在先访问节点，再访问左右子树
        this.preorderTraverse(root, handler);
    }

    private void preorderTraverse(Node node, Consumer handler) {
        if (node == null) return;
        handler.accept(node);  // 先访问节点
        this.preorderTraverse(node.left, handler);  // 再访问左右子树。如果是中序遍历就是把上面访问节点的语句放在这句下面
        this.preorderTraverse(node.right, handler);  // 如果是后序遍历就是把上面访问节点的语句放在这句下面
    }

    public void preorderTraverseNR(Consumer handler) {  // 前序遍历的非递归实现；实际应用很少（一般都用递归）；中序和后序的非递归实现更复杂
        Stack<Node> stack = new Stack<>();  // 采用 stack 实现（levelOrderTraverseNR 采用 queue 实现）
        if (root == null) return;
        stack.push(root);
        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            handler.accept(curr);
            if (curr.right != null)
                stack.push(curr.right);  // 因为栈是后入先出，所以要先压入右子节点，再压入左子节点，让左子节点先出栈
            if (curr.left != null)
                stack.push(curr.left);
        }
    }

    public void inorderTraverseNR(Consumer handler) {  // 使用 stack 实现（对比 preorderTraverseNR 和 levelOrderTraverseNR）
        if (root == null)
            throw new IllegalArgumentException("inorderTraverse failed.");

        Stack<Node> stack = new Stack<>();
        Node curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // Step 1: 不断入栈左孩子，直到叶子节点
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();  // Step 2: 出栈一个节点并访问它
            handler.accept(curr);
            curr = curr.right;   // Step 3: 调转方向，开始处理右孩子
        }
    }

    public void postorderTraverseNR(Consumer handler) {
        if (root == null)
            throw new IllegalArgumentException("inorderTraverse failed.");

        Stack<Node> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node temp = stack.peek();
            if (temp.left != null) {
                stack.push(temp.left);
                temp.left = null;
            }
            else if (temp.right != null) {
                stack.push(temp.right);
                temp.right = null;
            }
            else {
                handler.accept(temp.e);
                stack.pop();
            }
        }
    }

    public void levelOrderTraverseNR(Consumer handler) {  // 层序遍历（广度优先遍历）
        Queue<Node> queue = new LinkedList<>();  // 使用链表模拟的 queue 实现（或者直接使用队列也行）（对比 preorderTraverseNR、inorderTraverseNR）
        queue.add(root);
        while (!queue.isEmpty()) {
            Node curr = queue.remove();
            handler.accept(curr);
            if (curr.left != null)
                queue.add(curr.left);
            if (curr.right != null)
                queue.add(curr.right);
        }
    }

    public void levelOrderTraverse(Consumer handler) {  // 层序遍历的递归实现
        if (root == null) return;
        handler.accept(root);
        levelOrderTraverse(root, handler);
    }

    private void levelOrderTraverse(Node curr, Consumer handler) {
        if (curr.left != null)
            handler.accept(curr.left);
        if (curr.right != null)
            handler.accept(curr.right);
        if (curr.left != null)
            levelOrderTraverse(curr.left, handler);
        if (curr.right != null)
            levelOrderTraverse(curr.right, handler);
    }

    /*
    * Misc
    * */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        toString(root, s, 0);
        return s.toString();
    }

    private void toString(Node node, StringBuilder s, int depth) {
        if (node == null) return;

        s.append("\n");
        for (int i = 0; i < depth; i++)
            s.append("--");
        s.append(node.toString());

        toString(node.left, s, depth + 1);
        toString(node.right, s, depth + 1);
    }
}
