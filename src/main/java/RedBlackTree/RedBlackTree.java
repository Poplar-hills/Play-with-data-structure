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
*   5. 任意节点到其后代的叶子节点的路径上都包含相同个数的黑色节点（这条是红黑树的核心理念）。
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
*   4. 绝对平衡树 —— 2-3树具有保持绝对平衡的机制（也是我们讲的唯一一种绝对平衡树），从根节点到任意叶子节点所经过的节点数总是相同的，或者
       说树的任意节点的左右子树高度相等（高度差为0）。
* - 2-3树的 add 操作的精妙机制是其保持绝对平衡的根本原因，SEE：https://coding.imooc.com/lesson/207.html#mid=15081（非常精妙！）
*
* - 但是要让一个节点存放1-2个值、具有2-3个子节点，在编程上会很复杂，而红黑树就由此而来 —— 用来化简2-3树的编程复杂度。
* - 具体做法是将2-3树中的3节点用一红、一黑两个节点表示。例如，上图中的 17,33 这个3节点在红黑树中就会表示成红色的17节点和黑色的33节点。
*   因此整棵树就变成了这样：
*                         43
*                     /       \
*               17 - 33        50         - 其中 17 是红节点
*              / |   \      /     \
*        6 - 12  18  37   48  66 - 88     - 其中 6 和 66 是红节点
*
* - 这样即化简了2-3树的复杂度，又保留了2-3树的绝对平衡的本质。
* - 但要注意：将2-3树的一个3节点拆成红黑树上的两个节点，这使得红黑树不再是标准的平衡二叉树，它保持的是“黑平衡”（即只考虑黑节点时是平衡
*   的）。此时再来理解上面红黑树定义的第5条，因为2-3树是绝对平衡树，因此树上任意节点到叶子节点经过的节点数都相同。而红黑树上的红节点是
*   融合节点，因此如果不算红节点（或将红节点和旁边的黑节点看做一个黑节点），则红黑树上的每个节点就相当于对应的2-3树上的每个节点（这就是
*   前面说的“黑平衡”）。因此就有了第5条定义。
* - 任意一棵2-3树都可以通过这样的方式转化成一颗红黑树。
* - 这里我们讲解和实现的都是“左倾红黑树”，即红色节点是黑节点的左子节点；实际上也可以实现成“右倾红黑树”。
*   具体解释 SEE：https://coding.imooc.com/lesson/207.html#mid=15413
*
* - 红黑树复杂度分析：
*   - 如果有 n 个节点，在最坏情况下（一个路径上每个黑节点旁边都有一个红节点），则此时红黑树的最大高度是 2logn，因此增删改查操作的复杂
*     度还是 O(logn) 级别的。
*   - 因为最大高度为 2logn，大于 AVL 的最大高度 logn，因此在红黑树上的查询操作会比 AVL 树慢一点。而红黑树的添加、删除操作是要比 AVL
*     树快的，因此：
*     - 如果需要在树上频繁添加、删除元素的情况，用红黑树效率更高；
*     - 如果树上的元素几乎不会变动，在整棵树被创建完之后只做查询操作的话，用 AVL 树效率更高。
*
* - 红黑树与 BST、AVL 树的比较：
*   - 对于完全随机的数据，普通的 BST 就很好用了（完全随机数据不会使 BST 退化成链表），不需要那么多复杂的维持平衡的逻辑。
*   - 对于查询、更新（get、set、contains）操作较多的情况，AVLTree 很好用。
*   - 红黑树牺牲了平衡性（其高度为 2logn），但换来了增、删操作中更少的旋转操作（比 AVL 树更少），因此对于频繁的增、删的情况，性能要
*     优于 AVLTree。并且其统计性能（同一复杂度级别内增、删、改、查的综合性能统计）也优于 AVL 树。因此多用于各种语言中有序的数据结构
*     （如 TreeMap、TreeSet）的底层实现。
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
            color = RED;  // 默认新添加的节点是红节点，因为红节点在红黑树中表示的是融合节点，而新节点被添加之后总是要和一个叶子节点进行融合，因此新节点默认是红色。
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
            return BLACK;  // 因为在红黑树上红节点表示的是融合节点，而 null 节点不是融合节点，因此 null 是黑节点（或见上面红黑树定义第三条）。
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
    *
    *       node                         x
    *      /    \     对 node 左旋      /  \
    *    T1      x     -------->    node   T3
    *          /  \                /   \
    *        T2   T3             T1    T2
    * */
    private Node leftRotate(Node node) {  // 当一个红节点（x）在一个黑节点（node）右侧的时候，我们的红黑树定义被打破，因此需要左旋。
        Node x = node.right;
        Node T2 = x.left;
        // 左旋
        node.right = T2;
        x.left = node;
        // 维护颜色（这是 BST 没有的步骤）
        x.color = node.color;  // 现在的父节点 x 的颜色要与之前的父节点 node 的颜色保持一致
        node.color = RED;  // 左旋之前两个节点是融合状态（一红一黑），左旋之后应该还是这个状态，因此此时 node 应该变为红色

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
        // 右旋
        node.left = T1;
        x.right = node;
        // 维护颜色
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
     * - 可视化解释 SEE:
     *   1. https://coding.imooc.com/lesson/207.html#mid=15184（0'0''开始）
     *   2. https://coding.imooc.com/lesson/207.html#mid=15185（0'0''开始，一定要看！）
     * - 向红黑树中添加节点有3种情况：
     *   1. 向一棵空树添加新节点：直接添加，并将新添加的红节点置黑。
     *   2. 向一个黑色叶子节点添加新节点（相当于向2-3树中的一个2节点添加新节点）：
     *     a. 若新添加的红节点 < 该黑节点，则直接添加到其左侧。
     *     b. 若新添加的红节点 > 该黑节点，则应添加到其右子树，但这样红节点就在黑节点右侧而打破了左倾红黑树的定义，因此要对黑节点左旋。
     *   3. 向一个左边带有一个红节点的黑节点添加新节点（相当于向2-3树中的一个3节点添加新节点，形成一个待分裂的4节点）：
     *     a. 若新添加的红节点 > 该黑节点，则添加到其右侧，形成一黑带两红的状态。此时同样是红节点出现在黑节点右侧而打破了左倾红黑树
     *        的定义，因此要将这3个节点进行颜色翻转（相当于2-3树中临时的4节点分裂成3个2节点，作为父节点的2节点再向上进行融合，因此
     *        必须是红色，下面2个2节点是黑色）。
     *     b. 若新添加的红节点 < 该黑节点，则添加到其左侧，形成一个左倾链表的状态，此时要对黑节点右旋，形一黑带两红的状态（同 a 的
     *        情况），因此还需要将这3个节点进行颜色翻转。
     *     c. 若左边的红节点 < 新添加的红节点 < 黑节点，则应添加到已有红节点的右子树，形成一个小于号的形状。此时要先对中间的红节点左旋，
     *        再对黑节点右旋，最后再将这3个节点进行颜色翻转。
     * - 在实现的时候，这3种情况可以使用同一个逻辑链条串起来：https://coding.imooc.com/lesson/207.html#mid=15185（4'51''）
     */
    public void add(K key, V value) {
        root = add(root, key, value);
        root.color = BLACK;  // 保持根节点是黑节点（见红黑树定义第二条）
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

        // 此时进行红黑树平衡性的维护（这段逻辑统一了上面注释中的3种情况，只需要根据当前树的形态和颜色判断是否需要进行某种操作即可）。
        if (isRed(node.right) && !isRed(node.left))  // 是否需要左旋
            node = leftRotate(node);
        if (isRed(node.left) && isRed(node.left.left))  // 是否需要右旋
            node = rightRotate(node);
        if (isRed(node.left) && isRed(node.right))  // 如果是一黑带两红，则需要颜色翻转，让2个子节点变黑，父节点变红，再向上融合。
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
