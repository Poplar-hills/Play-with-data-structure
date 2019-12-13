package UnionFind;

/*
 * - 在 UnionFind2 这个实现主要解决的是 UnionFind1 中 union 操作的时间复杂度是 O(n) 的问题。
 *   这一版的实现才是并查集真正的实现思想。
 *
 * - 思路：将数组中的每个元素看做一个节点，节点之间相连构成一棵树，这棵树结构较为特殊：
 *   1. 是由子节点指向父节点
 *   2. 在进行 union 操作时（如下面图4、5的过程 union(4,5)），需将4所在的树的根节点0指向5所在的树的根节点2。
 *      演示过程 SEE: https://coding.imooc.com/lesson/207.html#mid=14167 (3'00'')
 *                                                                                                                              1  2
 *                                  0  1  2     4  5               0  1  2     4                 0  1  2                          /|\
 *   0  1  2  3  4  5   union(3,2)        |            union(5,3)       / \         union(4,0)   |    / \         union(4,5)     3 5 0
 *   ----------------      --->           3               --->         3   5           --->      4   3   5           --->            |
 *   0  1  2  3  4  5               ----------------               ----------------              ----------------                    4
 *                                  0  1  2  2  4  5               0  1  2  2  4  2              0  1  2  2  0  2            ----------------
 *                                                                                                                           2  1  2  2  0  2
 * - 这样实现的并查集的 union(p,q) 和 isConnected(p,q) 操作的时间复杂度都是 O(h)，其中 h 为 p, q 所在的树的最大高度。
 *   这得益于 find 方法的复杂度是 O(h)。
 * - 比起 UnionFind1，UnionFind2 中的 union 操作复杂度有所减小，而 isConnected 复杂度增大。
 * */

public class UnionFind2 implements UF {
    private int[] parents;

    public UnionFind2(int size) {
        parents = new int[size];
        for (int i = 0; i < size; i++)
            parents[i] = i;
    }

    @Override
    public void union(int p, int q) {  // 合并两个元素所属的集合，O(h) 复杂度
        int pRoot = find(p);           // 比如 p 是上图4中的4，则 pRoot 是0
        int qRoot = find(q);           // 比如 q 是上图4中的3，则 qRoot 是2

        if (pRoot == qRoot) return;

        parents[pRoot] = qRoot;        // 将 p 所在的根节点的集合编号置为 q 所在的根节点的集合编号
    }

    private int find(int p) {          // 查找元素 p 的集合编号（递归实现），O(h) 复杂度。（用图5中的4作为例子来理解）
        if (p < 0 || p >= parents.length)
            throw new IllegalArgumentException("getSetId failed. p is out of bound.");
        return parents[p] == p ? p : find(parents[p]);  // 只有根节点的值等于其 set id ∴ 用这条性质判断是否到达根节点
    }

    private int findNR(int p) {        // 查找元素 p 的集合编号（非递归实现）
        if (p < 0 || p >= parents.length)
            throw new IllegalArgumentException("getSetIdNR failed. p is out of bound.");

        while (parents[p] != p)
            p = parents[p];
        return p;
    }

    @Override
    public boolean isConnected(int p, int q) {  // 查看两个元素是否属于同一个集合，O(h) 复杂度
        return find(p) == find(q);
    }

    @Override
    public int getSize() { return parents.length; }
}
