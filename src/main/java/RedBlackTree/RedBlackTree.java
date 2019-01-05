package RedBlackTree;

/*
* - 红黑树是最负盛名的平衡二分搜索树（和）。
* - Java 的 TreeMap、TreeSet 底层使用的就是红黑树。
* - 红黑树的发明人 Robert Sedgewick 就是《算法》的作者，也是 Donald Knuth（高德纳）的学生。
* - 算法导论中的定义：A red-black tree is a BST that satisfies the following properties:
*   1. 每个节点非黑即红。
*   2. 根节点是黑色的。
*   3. 所有叶子节点（指的是 null 节点）都是黑色的。
*   4. 如果一个节点是红色，则它的两个子节点就都是黑色的。
*   5. 任意节点到其后代的叶子节点的路径上都包含相同个数的黑色节点。
*   其中第5条是红黑树定义的核心。
*
* - 但是这种定义比较生硬，没有解释为什么会出现这样一种数据结构。更好的解释出自《算法》，其中首先探索了另一种平衡树 —— 2-3树。
* - 2-3树与红黑树是等价的，理解了2-3树和红黑树之间的等价关系就理解了上面这5条性质的由来。同时理解了2-3树对于理解 B 树也有巨大帮助。
*
* - 2-3树：A 2–3 tree is a B-tree with either 2 or 3 elements per node.
*                         43
*                     /        \
*                 17,33         50
*               /   |   \     /    \
*            6,12   18  37   48  66,88
*
* - 2-3树具有以下性质：
*   1. 满足搜索树的基本性质 —— 小于节点值的元素放在左边，大于节点值的元素放在右边。
*   2. 每个节点可能是"2节点"或"3节点"（有2个或3个子节点），可以存放1个或2个元素。
*   3. 每个2节点都有3个子节点，左子节点比2节点中的元素都要小；右子节点比2节点中的元素都要大；中间子节点大小处于2节点的两个元素大小之间。
*   4. 具有保持绝对平衡的机制 —— 从根节点到任意叶子节点所经过的节点数总是相同的（类似完美二叉树）（这是我们讲的唯一一种绝对平衡树）
* - 2-3树的 add 操作的精妙机制是其保持绝对平衡的根本原因，SEE：https://coding.imooc.com/lesson/207.html#mid=15081（非常精彩！）
*
* - 但是要让一个节点存放1-2个值、具有2-3个子节点，在编程上会很复杂，而红黑树就由此而来 —— 用来化简2-3树的编程复杂度。
* - 具体做法是将2-3树中的3节点用一红、一黑两个节点表示，例如上图中的 17,33 这个3节点在红黑树中就会表示成红色的17节点和黑色的33节点。
*   因此整棵树就变成了这样：
*                         43
*                     /       \
*               17 - 33        50         其中 17 是红节点
*              / |   \      /     \
*        6 - 12  18  37   48  66 - 88     其中 6 和 66 是红节点
*   这样即化简了2-3树的复杂度，又保留了2-3树的功能。但要注意：将2-3树的一个3节点拆成两个节点使得红黑树不再是标准的平衡二叉树，它保持的
*   是"黑平衡"（即只考虑黑节点时是平衡的），因此可以说它牺牲了平衡性来获得更好的统计性能。
* - 任意一棵2-3树都可以通过这样的方式转化成一颗红黑树。
* - 这里我们讲解和实现的都是"左倾红黑树"，即红色节点是黑节点的左子节点；实际上也可以实现成"右倾红黑树"。
*   具体解释 SEE：https://coding.imooc.com/lesson/207.html#mid=15413
*
* - 红黑树复杂度分析：
*   - 如果有 n 个节点，在最坏情况下（每个黑节点旁边都有一个红节点），则树的最大高度是 2logn，因此增删改查操作的复杂度都是 O(logn) 级别的。
*   - 因为最大高度为 2logn，大于 AVL 的最大高度 logn，因此在红黑树上的查询操作会比 AVL 树慢一点。而红黑树的添加、删除操作
*     是要比 AVL 树快的。因此，如果需要在树上频繁添加、删除元素的情况，用红黑树效率会更高。而如果树上的元素几乎不会变动，在
*     整棵树被创建完之后只做查询操作的话，用 AVL 树效率更好一点。
*   - 与 BST、AVL 的比较：
*     - 对于完全随机的数据，普通的 BST 就很好用了（完全随机数据不会使 BST 退化成链表），不需要那么多复杂的维持平衡的逻辑。
*     - 对于查询、更新（get、set、contains）操作较多的情况，AVLTree 很好用。
*     - 红黑树的统计性能（统一复杂度级别内的性能统计）最优，即综合了增、删、改、查操作。
*   - 注：我们这里实现的红黑树并不是性能最优的版本，只是为了讲解其原理。
* */

public class RedBlackTree<K extends Comparable<K>, V> {  // 此处实现的红黑树的节点是键值对，不同于之前实现的 BST 或 AVLTree（当然也可以实现成只有键的）
    private Node root;
    private int size;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        public K key;
        public V value;
        public Node left, right;
        public boolean color;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            color = RED;  // 新添加的节点都是红色，原因 SEE：https://coding.imooc.com/lesson/207.html#mid=15183（4分30秒处）
        }

        @Override
        public String toString() { return key.toString() + ": " + value.toString(); }
    }

    public RedBlackTree() {
        root = null;
        size = 0;
    }

    /*
     * 辅助方法
     */
    private boolean isRed(Node node) {
        if (node == null)  // 处理节点为空的情况
            return BLACK;
        return node.color;
    }

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
    *       node                         x
    *      /    \     对 node 左旋      /  \
    *    T1      x     -------->    node   T3
    *          /  \                /   \
    *        T2   T3             T1    T2
    * */
    private Node leftRotate(Node node) {
        Node x = node.right;
        Node T2 = x.left;

        // 左旋
        node.right = T2;
        x.left = node;

        // 维护颜色
        x.color = node.color;
        node.color = RED;

        return x;
    }

    /*
     *           node                        x
     *          /    \    对 node 右旋     /    \
     *        x      T2    -------->     y     node
     *      /  \                              /    \
     *     y   T1                            T1    T2
     * */
    private Node rightRotate(Node node) {
        Node x = node.left;
        Node T1 = x.right;

        node.left = T1;
        x.right = node;

        x.color = node.color;
        node.color = RED;

        return x;
    }

    private Node flipColors(Node node) {
        node.color = RED;
        node.left.color = node.right.color = BLACK;
        return node;
    }

    /*
     * 增操作
     * 可视化解释 SEE:
     *   1. https://coding.imooc.com/lesson/207.html#mid=15184（0分0秒开始）
     *   2. https://coding.imooc.com/lesson/207.html#mid=15185（0分0秒开始，一定要看！）
     * 在 add 过程中：
     *   1. 如果红黑树为空（无节点），则直接添加节点，并将新添加的红节点置黑。
     *   2. 如果新节点要添加到一个叶子节点上：
     *     a. 这个叶子节点只包含一个无子节点的黑节点（相当于2-3树中的2节点）：
     *       I. 如果新添加的红色节点的节点值小于叶子节点，则添加到其左侧；
     *       II. 如果大于该叶子节点，按理应该添加到右侧，而这样一来红色节点就在黑色的叶子节点右侧而打破了左倾红黑树定义（我们这里实现的
     *           是左倾红黑树），因此需要对叶子节点左旋。
     *     b. 这个叶子节点包含一个带有一个红色子节点的黑节点（红节点一定是黑节点的左子节点）（相当于2-3树中的3节点）：
     *       I. 如果新节点的值 > 黑节点，则可以直接添加到黑节点右侧，形成一黑带两红的情形（相当于2-3树中的4节点，需要分裂），然后要
     *           将这3个节点进行颜色翻转（相当于2-3树中的4节点分裂成3个2节点，作为父节点的2节点再向上进行融合）。
     *       II. 如果新节点的值 < 黑节点，同时也 < 红节点，则新节点会添加到红节点的左侧，形成一棵左倾的树（相当于2-3树中，一个2节点正
     *           在融合到上一层），然后要对黑节点右旋，最后再将这3个节点进行颜色翻转。
     *       III. 如果新节点的值 < 黑节点，同时 > 红节点，则新节点会添加到y已有红节点的右侧（仍然相当于2-3树中，一个2节点正在融合到上
     *           一层），然后要对中间的红节点左旋，再对黑节点右旋，最后再将这3个节点进行颜色翻转。
     */
    public void add(K key, V value) {
        root = add(root, key, value);
        root.color = BLACK;
    }

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

        // 维护红黑树平衡性及节点颜色（这段逻辑统一了上述注释中的各种情况，向2-3树中的无节点、2节点、3节点添加新节点的情况）
        if (isRed(node.right) && !isRed(node.left))  // 适用于 2.a.I 及 2.b.III 情况
            node = leftRotate(node);
        if (isRed(node.left) && isRed(node.left.left))  // 适用于 2.b.II 及 2.b.III 情况
            node = rightRotate(node);
        if (isRed(node.left) && isRed(node.right))  // 适用于 2.b.I, 2.b.II, 2.b.III 情况
            flipColors(node);

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
