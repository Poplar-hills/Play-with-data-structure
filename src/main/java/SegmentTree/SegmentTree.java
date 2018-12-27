package SegmentTree;

/*
* - 线段树（也叫区间树 Interval tree）解决的问题：区间内的统计查询。
*   - 其中，区间是固定的，而区间内每个片段上的数据是动态的。
*   - 例子：
*     1. 在一段长度确定的墙上，每次对不同区间内的墙面刷上不同颜色的油漆，问 m 次操作后，在 [i, j] 区间内共可以看到多少种颜色？
*     2. 在2017年注册的所有用户中，找出至今消费最多的用户？停留时间最长的用户？（"2017年注册的用户"是固定区域，"至今消费额"是变量）
*     3. 在一段太空区域中的天体总量？（"一段太空区域"是固定区域，"区域中的天体总量"是变量）
*   - 线段树能让我们即使在大数据量的情况下，也能快速找到我们关心的区间中的节点值，并对它们进行预定义的操作和统计。
*
* - 线段树有两个基本操作：更新（set）、查询（query）。可以使用两种数据结构解决：
*                数组      线段树
*       更新     O(n)     O(logn)
*       查询     O(n)     O(logn)
*   - 数组完全可以实现功能，如在更新时，只需要去遍历找到需要更新的元素然后给其赋值即可；查询的时候也遍历数组找到区间内的所有值。
*   - 而线段树本质是平衡二叉树，因此增删改查的效率都比数组高很多（O(n) 和 O(logn) 是天壤之别）。
*
* - 线段树形态如下：
*                         fn(A[0..9])
*                     /               \
*            fn(A[0..4])             fn(A[5..9])
*            /       \                /       \
*     fn(A[0..1])  fn(A[2..4])  fn(A[5..6])  fn(A[7..9])    - 上层的每个节点是根据业务逻辑对左右孩子进行自定义处理后的结果
*       /   \       /    \       /    \      /    \
*    A[0]  A[1]  A[2]  A[3,4]  A[5]  A[6]  A[7]  A[8,9]     - 如果元素个数为 2^k 个（如8个），即满二叉树，到这层就完了
*                       /  \                      /  \
*                    A[3]  A[4]                A[8]  A[9]   - 如果元素个数为 2^k + 1 个，则不是满二叉树，就会多出这一层
*
* - 线段树是一棵平衡二叉树（Balanced Binary Tree），具有如下特性：
*   - 定义：对于任意叶子节点，其最大深度与最小深度的差不超过1，并且左右两个子树都是一棵平衡二叉树。
*   - 它很好的解决了 BST 可能退化成链表的问题，把增、删、查时的最好情况、最坏情况下的时间复杂度都维持在 O(logN) 级别。
*   - 完全二叉树一定是一棵平衡二叉树，反过来则不一定。
*
* - 就像完全二叉树（堆）一样，平衡二叉树也可以用数组表示 —— 即把平衡二叉树看成一棵完美二叉树（将最后一层不满的节点补上空节点）。
*   - 因为线段树的区间是固定的，因此不需要使用动态数组，使用普通数组即可。
*   - 因为一棵 h 层的完美二叉树共有 2^h - 1 个节点，而最后一层有 2^(h - 1) 个节点，因此可得：完美二叉树最后一层的节点数
*     约等于上面所有层的节点数之和（2^h - 1 可近似看做 2^h，是 2^(h - 1) 的2倍）。
*   - 因此，如果一个区间中有 n 个元素，若要在这个区间上建立线段树，并且使用数组来表示该线段树：
*     - 若 n 是2的倍数（n = 2^k），则需开辟 2n 的空间（最底层有 n 个元素，上面几层也是 n 个元素）。
*     - 若 n = 2^k + 1（非满二叉树情况，如上图），则需开辟 4n 的空间（为多出的那一行多开辟 2n 的空间，上面也是 2n）。
*   - 结论：如果某个区间上有 n 个元素，要为该区间建立线段树，并用数组表示，最坏情况下需要开辟 4n 的空间
*     - 会有空间浪费，因为最后一层要补空节点（最坏情况下会浪费近一半空间）。
*     - 但是线段树的意义就是在于用空间换时间，因此不需要太过在意。
* */

public class SegmentTree<E> {
    private E[] data;  // 数组副本
    private E[] tree;  // 以数组表示的线段树（是补全最底层之后的完美二叉树）
    private Merger<E> merger;

    public SegmentTree(E[] arr, Merger<E> merger) {  // 由数组生成线段树（但必须是包装类数组）
        this.merger = merger;

        data = (E[]) new Object[arr.length];  // 维护一个传入的数组副本
        for (int i = 0; i < arr.length; i++)
            data[i] = arr[i];

        tree = (E[]) new Object[arr.length * 4];  // 给 n 个元素开辟 4n 的空间

        buildSegmentTree(0, 0, data.length - 1);
    }

    private void buildSegmentTree(int treeIndex, int l, int r) {  // 从 treeIndex 处开始在 tree 中创建表示区间 [l..r] 的线段树
        if (l == r) {  // 终止条件：当递归到叶子节点，只剩下一个元素时 l == r
            tree[treeIndex] = data[l];
            return;
        }
        // 递归逻辑：要计算 treeIndex 处节点的值
        // -> 需找到该节点的左右孩子节点，根据自定义的业务逻辑对他们求值
        //   -> 需找到左右孩子节点的索引，根据索引从 tree 上取值
        //     -> 需计算出左右孩子节点的值，并赋到对应的索引上
        //       -> 需为每个孩子节点确定区间边界
        int leftTreeIndex = getLeftChildIndex(treeIndex);
        int rightTreeIndex = getRightChildIndex(treeIndex);

        int mid = (r - l) / 2 + l;  // 实际上 (r + l) / 2 就可以，但是当 r 和 l 极大时会发生整型溢出，所以使用这里的标准写法。
        buildSegmentTree(leftTreeIndex, l, mid);
        buildSegmentTree(rightTreeIndex, mid + 1, r);

        tree[treeIndex] = merger.merge(tree[leftTreeIndex], tree[rightTreeIndex]);
    }

    /*
    * 辅助函数
    * - 和我们之前实现的堆一样，因为都是使用数组表示树形结构，因此需要一种在数组和树结构之间的对应方式。
    * - 在堆中还多了一个 getParentIndex 方法，这个方法在线段树中不需要，因此不写。
    * */
    private int getLeftChildIndex(int index) { return index * 2 + 1; }

    private int getRightChildIndex(int index) { return getLeftChildIndex(index) + 1; }

    /*
    * 改操作
    * */
    public void set(int index, E e) {
        if (index < 0 || index >= data.length)
            throw new IllegalArgumentException("set failed. Invalid index");

        data[index] = e;
        set(0, 0, data.length - 1, index, e);
    }

    private void set(int treeIndex, int l, int r, int index, E e) {
        if (l == r) {
            tree[treeIndex] = e;
            return;
        }

        int mid = (r - l) / 2 + l;
        int leftChildIndex = getLeftChildIndex(treeIndex);
        int rightChildIndex = getRightChildIndex(treeIndex);

        if (index <= mid)  // 在左边
            set(leftChildIndex, l, mid, index, e);
        else               // 在右边
            set(rightChildIndex, mid + 1, r, index, e);

        tree[treeIndex] = merger.merge(tree[leftChildIndex], tree[rightChildIndex]);
    }

    /*
    * 查操作
    * */
    public E query(int queryL, int queryR) {
        if (queryL < 0 || queryL >= data.length || queryR < 0 || queryR >= data.length || queryL > queryR)
            throw new IllegalArgumentException("query failed. Invalid query boundaries");
        return query(0, 0, data.length - 1, queryL, queryR);
    }

    private E query(int treeIndex, int nodeL, int nodeR, int queryL, int queryR) {
        if (nodeL == queryL && nodeR == queryR)
            return tree[treeIndex];

        int mid = (nodeR - nodeL) / 2 + nodeL;
        int leftTreeIndex = getLeftChildIndex(treeIndex);
        int rightTreeIndex = getRightChildIndex(treeIndex);

        if (queryR <= mid)  // 查询范围只在左子树的范围内
            return query(leftTreeIndex, nodeL, mid, queryL, queryR);
        if (queryL >= mid + 1)  // 查询范围只在右子树的范围内
            return query(rightTreeIndex, mid + 1, nodeR, queryL, queryR);

        // 查询范围同时存在于左、右子树的范围内
        E leftResult = query(leftTreeIndex, nodeL, mid, queryL, mid);
        E rightResult = query(rightTreeIndex, mid + 1, nodeR, mid + 1, queryR);
        return merger.merge(leftResult, rightResult);
    }

    public E get(int index) {
        if (index < 0 || index >= data.length)
            throw new IllegalArgumentException("get failed. Invalid index");
        return data[index];
    }

    public int getSize() { return data.length; }

    /*
     * Misc
     * */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (int i = 0; i < tree.length; i++) {
            s.append(tree[i]);
            if (i != tree.length - 1)
                s.append(", ");
        }
        s.append("]");
        return s.toString();
    }
}
