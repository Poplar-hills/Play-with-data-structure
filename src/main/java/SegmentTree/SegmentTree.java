package SegmentTree;

/*
* - 经典线段树问题：区间染色 —— 在一段定长区间内，每次对区间内的不同片段刷上不同颜色，求：
*   1. m 次操作后共可以看到多少种颜色？
*   2. m 次操作后，在 [i, j] 区间内共可以看到多少种颜色？
* - 更实际问题：基于区间的统计查询
*   1. 在2017年注册的所有用户中，找出至今消费最多的用户？停留时间最长的用户？（"2017年注册的用户"相当于固定区域，"至今消费额"是变量）
*   2. 在一段太空区域中的天体总量？（"一段太空区域"是固定区域，"区域中的天体总量"是变量）
* - 这里问题有两个基本操作：更新（set）、查询（query）。可以使用两种数据结构解决：
*              数组      线段树
*     更新     O(n)     O(logn)
*     查询     O(n)     O(logn)
*
* - 线段树形态如下：
*                         fn(A[0..9])
*                     /               \
*            fn(A[0..4])             fn(A[5..9])
*            /       \                /       \
*     fn(A[0..1])  fn(A[2..4])  fn(A[5..6])  fn(A[7..9])    - 上层的每个节点是根据业务逻辑对左右孩子进行自定义处理后的结果
*       /   \       /    \       /    \      /    \
*    A[0]  A[1]  A[2]  A[3,4]  A[5]  A[6]  A[7]  A[8,9]     - 如果元素个数为 2^k 个（如8个），则是满二叉树，到这层就完了
*                       /  \                      /  \
*                    A[3]  A[4]                A[8]  A[9]   - 如果元素个数为 2^k + 1 个，则不是满二叉树，就会多出这一层
*
* - 线段树是一棵平衡二叉树（Balanced Binary Tree），具有如下特性：
*   - 定义：最大深度与最小深度的差不超过1，并且左右两个子树都是一棵平衡二叉树。
*   - 它很好的解决了 BST 可能退化成链表的问题，把增、删、查时的最好情况、最坏情况下的时间复杂度都维持在 O(logN) 级别。
*   - 完全二叉树一定是一棵平衡二叉树，反过来则不一定。
*   - 就像完全二叉树一样，平衡二叉树也可以用数组表示 —— 即把平衡二叉树看成一棵满二叉树（将最后一层不满的节点补上空节点）。
*     - 因为一棵 h 层的满二叉树共有 2^h - 1 个节点，而最后一层有 2^(h - 1) 个节点，因此可得：满二叉树最后一层的节点数
*       大致等于上面所有层的节点数之和（2^h - 1 可近似看做 2^h，是 2^(h - 1) 的2倍）。
*     - 因此可以算出使用数组表示平衡二叉树的时候需要开辟多大的空间：
*       - 若数组中共有 n 个元素，且 n 是2的倍数（n = 2^k），则需开辟 2n 的空间。
*       - 若数组中共有 n 个元素，且 n = 2^k + 1（非满二叉树情况，如上图），则可以开辟 4n 的空间（为多出的那一行多开辟 2n 的空间）
* - 因此，如果某个区间上有 n 个元素，要将该数组转化成线段树，最坏情况下需要开辟 4n 的空间
*   - 会有空间浪费，因为最后一层要补空节点（最坏情况下会浪费近一半空间）。
*   - 但是线段树的意义就是在于用空间换时间，因此不需要太过在意。
* */

public class SegmentTree<E> {
    private Merger<E> merger;
    private E[] data;
    private E[] tree;

    public SegmentTree(E[] arr, Merger<E> merger) {  // 由数组生成线段树
        this.merger = merger;

        data = (E[]) new Object[arr.length];
        for (int i = 0; i < arr.length; i++)
            data[i] = arr[i];

        tree = (E[]) new Object[arr.length * 4];  // 给 n 个元素开辟 4n 的空间

        buildSegmentTree(0, 0, data.length - 1);
    }

    private void buildSegmentTree(int treeIndex, int l, int r) {  // 在 treeIndex 位置创建表示区间 [l..r] 的线段树
        // 终止条件：当递归到叶子节点，只剩下一个元素时
        if (l == r) {
            tree[treeIndex] = data[l];
            return;
        }

        // 递归逻辑：计算 treeIndex 处节点的值
        // -> 需找到该节点的左右孩子节点，根据自定义的业务逻辑对他们进行求值
        //   -> 需找到左右孩子节点的索引，从索引上取值
        //     -> 需计算出左右孩子节点的值，并赋到对应的索引上
        //       -> 需为每个孩子节点确定区间边界
        int leftTreeIndex = getLeftChildIndex(treeIndex);
        int rightTreeIndex = getRightChildIndex(treeIndex);

        int mid = (r - l) / 2 + l;
        buildSegmentTree(leftTreeIndex, l, mid);
        buildSegmentTree(rightTreeIndex, mid + 1, r);

        tree[treeIndex] = merger.merge(tree[leftTreeIndex], tree[rightTreeIndex]);
    }

    /*
    * 辅助函数
    * */
    private int getLeftChildIndex(int index) { return index * 2 + 1; }  // 同 MaxHeap

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

    private E query(int treeIndex, int l, int r, int queryL, int queryR) {
        if (l == queryL && r == queryR)
            return tree[treeIndex];

        int mid = (r - l) / 2 + l;
        int leftTreeIndex = getLeftChildIndex(treeIndex);
        int rightTreeIndex = getRightChildIndex(treeIndex);

        if (queryR <= mid)  // 查询范围只在左子树的范围内
            return query(leftTreeIndex, l, mid, queryL, queryR);
        if (queryL >= mid + 1)  // 查询范围只在右子树的范围内
            return query(rightTreeIndex, mid + 1, r, queryL, queryR);

        // 查询范围同时存在于左、右子树的范围内
        E leftResult = query(leftTreeIndex, l, mid, queryL, mid);
        E rightResult = query(rightTreeIndex, mid + 1, r, mid + 1, queryR);
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
