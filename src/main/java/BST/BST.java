package BST;

import java.util.HashSet;

/*
* Binary Search Tree 二分搜索树
*
* - Why?
*   - 使用 BST 通常是为了实现查找表（或叫字典），即键值对组合。
*   - 而查找表有多种实现：
*                      查找元素      插入元素      删除元素
*        Array          O(n)         O(n)        O(n)
*        Sorted Array   O(logn)      O(n)        O(n)    -> 使用二分查找可以以 O(logn) 的复杂度进行查找
*        BST            O(logn)      O(logn)     O(logn)
*        HashTable      O(1)         O(1)        O(1)
*
*   - 可见 BST：
*     1. 实现查找表的各种操作（查找、插入、删除）的效率很高，但不是最高的；
*     2. 元素具有顺序性，这是 HashTable 实现的查找表不具备的性质。
*
* - 3 Types of Binary Tree
*   1. 满二叉树（full/strict binary tree）：树中除了叶子节点，每个节点都有两个子节点（"满"意味着"子节点已满"）。
*   2. 完全二叉树（complete binary tree）：叶子节点只在最后两层，且最后一层的叶子节点均在最左边（"完全"意味着每次添加节点都是在"补完"这棵树）。
*   3. 完美二叉树（perfect binary tree）：即是完全二叉树，也是满二叉树，同时叶子节点均在最后一层（即形成了一个完美三角形）。
*
*              7                     7                     7
*            /   \                 /   \                 /   \
*           3     9               3     9               3     9
*          / \                  /  \   /  \            / \   / \
*         1   5                1    5 8   10          1   5 8  10
*            / \              / \  /
*           4   6            0  2 4
*           满二叉树               完全二叉树              完美二叉树
*
*   - 完全二叉树不一定是满二叉树（不一定每个节点都有两个子节点，如上图），反之亦然（如上图）；
*   - 完全二叉树因为每次添加节点都是在“补全”这棵树，因此永远不会退化成链表（即节点个数 n 和树高 h 之间永远都是 h=log(n) 关系）；
*   - 完美二叉树即是完全二叉树，也是满二叉树。
*
* - 树的平衡性：
*   - 定义：
*     - 堆、线段树中对"平衡"的定义：树中任意两个叶子节点的高度差不超过1（在这个定义下，上图的满二叉树就不平衡）。
*     - AVL 树中对"平衡"的定义：对于树中任何一个节点，其左右两个子树的高度差不超过1（比上面的定义松一些）。
*   - 举例：
*     - 完全二叉树、完美二叉树一定是平衡二叉树。
*     - 堆、AVL树、2-3树、红黑树、B树都是平衡树，而且都是自平衡树（添加节点之后仍然维持平衡）。
*     - 线段树也是平衡树，但不是自平衡树（没有 add 方法）。
*   - 意义：
*     - 如果说一棵树是“平衡”的，就意味着它的高度和节点数量成 h=log(n) 的关系。
*     - 更进一步来说，"平衡"是一个从操作复杂度角度定义的概念，即用来判断一棵树的操作复杂度是否是 O(logn)。例如说"堆是一种平衡树”，
*       实际上就意味着堆的各种操作（insert、extract）的复杂度都是 O(logn) 级别的”。
*
* - BST 满足3个条件：
*   1. 是二叉树。
*   2. 树上的每个节点：
*     a). 都 > 其左子树上的任意节点；
*     b). 都 < 其右子树上的任意节点；
*     c). ∵ 子节点要么 > 父节点，要么 < 父节点 ∴ BST 上一般不存在相等的节点。
*   3. 每个节点值必须具有可比较性（实现 Comparable 接口）。
*
* - BST 的遍历（使用上面的完美二叉树举例）：
*   - 前序：父->左->右。树上顺序：7->3->1->5->9->8->10（层级从上到下）。
*   - 中序：左->父->右。树上顺序：1->3->5->7->8->9->10（节点值从小到大）。
*   - 后序：左->右->父。树上顺序：1->5->3->8->10->9->7（层级从下到上）（后序遍的一个应用是为 BST 释放内存，即对于一个
*     节点需先释放所有子节点内存，再释放父节点内存，因此需要后续遍历。Java 是自动垃圾回收，所以不需要操心该问题，但 C++ 就要了）。
*   - 二叉树（而不只是 BST）的：
*     1. 深度优先遍历（DFS，例如前序遍历）通常采用递归实现，也可以采用迭代 + Stack 实现；
*     2. 广度优先遍历（BFS，即层序遍历）通常采用迭代 + Queue 实现，也可以采用递归实现。
*   - 对于树的遍历，无论是前、中、后序遍历，时间复杂度都是 O(n)，其中 n 是树中节点个数。
*
* - BST 的重要性质 —— 顺序性。BST 中存储元素是隐含顺序的（中序遍历会将节点值从小到大排列），因此
*   1. 基于 BST 实现的 TreeSet、TreeMap 类都是有顺序性的，而 HashSet、HashMap 则没有。
*   2. 基于顺序性，很容易实现一些其他有用的方法：
*     - getSuccessor、getPredecessor：给定一个值，找到其前驱、后继节点值。
*     - floor、ceil：给定一个值，找到比它小的最大节点值、比它大的最小节点值。
*     - rank：给定一个节点值，找到其在 BST 上的排名。
*     - select：给定一个排名，找到 BST 上该排名的节点值；
*     - 实现 rank, select 的最好方式是在节点类中维护一个 size 字段，从而可以记录以每个节点为根的 BST 中共有多少个元素。
*     - 除了在每个节点中维护 size 字段以外，还可以维护 depth、count 等字段来实现其他常用功能（比如 count 字段使得 BST 能支持重复元素）。
*     - 参考实现 SEE：https://coding.imooc.com/learn/questiondetail/63002.html（ceil 和 floor 的实现有问题）。
*
* - BST 的时间复杂度分析：SEE: Set - PerformanceTest
*
* - BST 的空间复杂度：O(h)，其中 h 是树的最大高度。但 h 的取值要具体分析：
*   - 若树平衡，则 h=log(n)；
*   - 若树不平衡并在最坏情况下退化成链表，则 h=n（树上有多少个节点树就有多高）。
*   - 因此严谨来说，BST 的空间复杂度在 O(logn) ~ O(n) 之间。
* */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

public class BST<E extends Comparable<E>> {  // 可比较的泛型
    private Node root;
    private int size;

    class Node {
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
    public void add(E e) { root = add(root, e); }  // 插入之后的结果是更新这棵树

    private Node add(Node node, E e) {
        if (node == null) {      // 递归到底
            size++;
            return new Node(e);  // 创建并返回新节点（递归函数每次返回更新后的子树）
        }

        if (e.compareTo(node.e) < 0)
            node.left = add(node.left, e);   // 更新左子树
        else if (e.compareTo(node.e) > 0)
            node.right = add(node.right, e); // 更新右子树（注意没有 == 0 的情况 ∵ 该 BST 的实现不允许重复节点）

        return node;                         // 每次递归返回的都是子树更新后的根节点
    }

    /*
    * 删操作
    * */
    // 删除最小节点
    public E removeMin() {
        if (root == null)
            throw new IllegalArgumentException("removeMin failed");
        E min = getMin();        // 先找到最小值，再删除之
        root = removeMin(root);
        return min;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {          // 左子树为 null 的节点即为最小值节点
            Node rightNode = node.right;  // 转移右子树（可能有也可能没有，这里统一操作）
            node.right = null;
            size--;
            return rightNode;             // 将右子树返回给上一层节点，作为其左子树
        }
        node.left = removeMin(node.left);  // 递归更新左子树
        return node;
    }

    // 删除最大节点
    public E removeMax() {
        if (root == null)
            throw new IllegalArgumentException("removeMax failed");
        E min = getMax();
        root = removeMax(root);
        return min;
    }

    private Node removeMax(Node node) {
        if (node.right == null) {       // 右子树为 null 的节点即为最大值节点
            Node leftNode = node.left;  // 转移左子树
            node.left = null;
            size--;
            return leftNode;
        }
        node.right = removeMax(node.right);
        return node;
    }

    // 删除节点值为 e 的节点
    public void remove(E e) {
        root = remove(root, e);
    }

    private Node remove(Node node, E e) {
        if (node == null) return null;           // 整棵树上没找到 e
        if (e.compareTo(node.e) < 0) {           // e 在左子树上
            node.left = remove(node.left, e);
            return node;
        } else if (e.compareTo(node.e) > 0) {    // e 在右子树上
            node.right = remove(node.right, e);
            return node;
        } else {                                 // 该节点就是待删除节点（递归的终止条件）
            if (node.left == null) {             // 若待删除节点只有右子树（或左右都没有），则转移右子树
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }
            if (node.right == null) {            // 若待删除节点只有左子树（或左右都没有），则转移左子树
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            }
            // 若待删除节点左右子树都有，则使用 Hibbard Deletion 方法（SEE: https://coding.imooc.com/lesson/207.html#mid=13477, 2'26）：
            // 1. 先找到后继节点（即比待删除节点大的最小节点，找前驱节点也可以，即比待删除节点小的最大节点）
            // 2. 用这个节点取代待删除节点的位置
            // 3. 删除待删除节点
            Node successor = getMin(node.right);      // 右子树中最小的就是后继节点
            successor.right = removeMin(node.right);  // removeMin 里已经 size-- 过 ∴ 后面不用再减了。此处顺序很重要，要先给右子树赋值，再给
            successor.left = node.left;               // 左子树赋值（SEE: https://coding.imooc.com/learn/questiondetail/84029.html）
            node.left = node.right = null;
            return successor;
        }
    }

    /*
     * 查操作
     * */
    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    // contains
    public boolean contains(E e) {
        return contains(root, e);
    }

    private boolean contains(Node node, E e) {
        if (node == null) return false;
        if (e.compareTo(node.e) == 0)  return true;
        Node nextToSearch = e.compareTo(node.e) < 0 ? node.left : node.right;
        return contains(nextToSearch, e);
    }

    // getMin
    public E getMin() {  // 获取 BST 的最小值。因为每个节点的左孩子都比该节点小，因此 BST 的最小值就在左下角的叶子节点上
        if (size == 0)
            throw new IllegalArgumentException("getMin failed. Empty tree");
        return getMin(root).e;
    }

    private Node getMin(Node node) {
        return node.left == null ? node : getMin(node.left);
    }

    // getMax
    public E getMax() {  // 同理 BST 的最大值就在右下角的叶子节点上
        if (size == 0)
            throw new IllegalArgumentException("getMax failed. Empty tree");
        return getMax(root).e;
    }

    private Node getMax(Node node) {
        return node.right == null ? node : getMax(node.right);
    }

    // 返回小于 e 的最大节点值（很好的练习，自己实现一下）
    public E floor(E e) {
        if (size == 0 || e.compareTo(getMin(root).e) < 0)  // 如果不存在 e 的 floor 值（树为空或 e 比树中的最小值还小）
            return null;
        return floor(root, e).e;
    }

    private Node floor(Node node, E e) {  // 思路是先一直往左下角搜索，若碰到 = e 的节点即找到，若碰到 > e 的节点则继续在其右子树中找
        if (node == null) return null;
        if (e.compareTo(node.e) == 0) return node;                // 若相等，则该 node 就是 floor
        if (e.compareTo(node.e) < 0) return floor(node.left, e);  // 若 e < node.e，则 floor 一定在左子树中
        Node candidate = floor(node.right, e);                    // 若 e > node.e，则 floor 要么在右子树中，要么就是 ndoe 本身
        return candidate != null ? candidate : node;              // （即要检查右子树中是否还有 < e 且 > node.e 的节点）

    }

    // 返回大于 e 的最小节点值（很好的练习，自己实现一下）
    public E ceiling(E e) {
        if (size == 0 || e.compareTo(getMin(root).e) < 0)
            return null;
        return ceiling(root, e).e;
    }

    private Node ceiling(Node node, E e) {  // 思路是先一直往右下角搜索，若碰到 = e 的节点即找到，若碰到 < e 的节点则继续在其左子树中找
        if (node == null) return null;
        if (e.compareTo(node.e) == 0) return node;
        if (e.compareTo(node.e) > 0) return ceiling(node.right, e);
        Node candidate = ceiling(node.left, e);
        return candidate != null ? candidate : node;
    }

    /*
     * Traverse
     * */
    // 前序遍历（递归实现）
    public void preorderTraverse(Consumer<Node> handler) {
        this.preorderTraverse(root, handler);
    }

    private void preorderTraverse(Node node, Consumer<Node> handler) {
        if (node == null) return;
        handler.accept(node);  // 先访问节点
        this.preorderTraverse(node.left, handler);  // 再访问左右子树。如果是中序遍历就是把上面访问节点的语句放在这句下面
        this.preorderTraverse(node.right, handler);  // 如果是后序遍历就是把上面访问节点的语句放在这句下面
    }

    // 前序遍历（非递归实现）：实际应用很少（一般都用递归）；中序和后序的非递归实现更复杂
    public void preorderTraverseNR(Consumer<Node> handler) {
        Stack<Node> stack = new Stack<>();  // 非递归的前中后序遍历都采用 stack 实现（levelOrderTraverseNR 采用 queue 实现）
        if (root == null) return;
        stack.push(root);
        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            handler.accept(curr);
            if (curr.right != null) stack.push(curr.right);  // ∵ 栈是后入先出 ∴ 要先入栈右子节点再入栈左子节点，让左子节点先出栈
            if (curr.left != null) stack.push(curr.left);
        }
    }

    // 中序遍历（非递归实现）（使用 Stack，对比 preorderTraverseNR 和 levelOrderTraverseNR）
    public void inorderTraverseNR(Consumer<Node> handler) {
        if (root == null)
            throw new IllegalArgumentException("inorderTraverse failed.");

        Stack<Node> stack = new Stack<>();
        Node curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // Step 1: 不断入栈左孩子，直到最左节点（不一定是叶子节点）
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();  // Step 2: 访问栈顶节点
            handler.accept(curr);
            curr = curr.right;   // Step 3: 调转方向，开始处理右子树
        }
    }

    // 后续遍历（非递归实现）：方法1
    public void postorderTraverseNR(Consumer<Node> handler) {
        if (root == null)
            throw new IllegalArgumentException("postorderTraverse failed.");

        Set<Node> set = new HashSet<>();    // 记录每个节点是否已被访问过
        Stack<Node> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node node = stack.pop();
            boolean isLeafNode = node.left == null && node.right == null;
            boolean leftDone = set.contains(node.left);
            boolean rightDone = set.contains(node.right);
            boolean childrenDone = (leftDone && rightDone) || (node.left == null && rightDone) || (node.right == null && leftDone);

            if (isLeafNode || childrenDone) {  // 若是叶子节点，或左右子节点已经被访问过，则访问当前节点，并加入 set
                handler.accept(node);
                set.add(node);
            } else {                           // 若不是叶子节点，且左右子节点中还有没被访问过的，则放回栈中待后面访问
                stack.push(node);
                if (node.right != null) stack.push(node.right);
                if (node.left != null) stack.push(node.left);
            }
        }
    }

    // 后续遍历（非递归实现）：方法2（不如方法1直观）
    public void postorderTraverseNR2(Consumer<Node> handler) {
        if (root == null)
            throw new IllegalArgumentException("postorderTraverse failed.");

        Stack<Node> stack = new Stack<>();
        Node prev = null, curr = root;  // 用一个 prev 指针记录上一次访问的节点（作用类似方法1中的 Set）

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // 先往左走到最左节点（不一定是叶子节点），一路上入栈所有节点
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            if (curr.right == null || curr.right == prev) {  // 若没有右子节点，或有右子节点但已经被访问过，则可以访问当前节点（∵ 上面保证了没有左子节点）
                handler.accept(curr);
                prev = curr;
                curr = null;       // 置空 curr 好跳过 while 循环
            } else {               // 若有右子节点且还未被访问过，则把该节点放回 stack 中，先遍历其右子节点
                stack.push(curr);
                curr = curr.right;
            }
        }
    }

    // 层序遍历（BFS）（非递归实现）
    public void levelOrderTraverseNR(Consumer<Node> handler) {
        Queue<Node> queue = new LinkedList<>();  // 广度优先遍历通常使用 queue 作为辅助结构（对比 preorderTraverseNR、inorderTraverseNR）
        queue.add(root);
        while (!queue.isEmpty()) {
            Node curr = queue.remove();
            handler.accept(curr);
            if (curr.left != null) queue.add(curr.left);
            if (curr.right != null) queue.add(curr.right);
        }
    }

    // 层序遍历（BFS）（递归实现）
    public void levelOrderTraverse(Consumer<Node> handler) {
        if (root == null) return;
        Queue<Node> q = new LinkedList<>();  // 对比非递归实现，该实现其实是用递归来模拟 while 循环
        q.offer(root);
        levelOrderTraverse(q, handler);
    }

    private void levelOrderTraverse(Queue<Node> q, Consumer<Node> handler) {  // 递归函数访问该节点，入队其子节点
        if (q.isEmpty()) return;
        Node curr = q.poll();
        handler.accept(curr);
        if (curr.left != null) q.offer(curr.left);
        if (curr.right != null) q.offer(curr.right);
        levelOrderTraverse(q, handler);
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
