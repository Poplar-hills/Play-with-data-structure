package UnionFind;

/*
* - 在 UnionFind2 这个实现主要解决的是 UnionFind1 中 union 操作的时间复杂度是 O(n) 的问题。
*   这一版的实现才是并查集真正的实现思想。
* - 实现思路是，将数组中的每个元素看做是一个节点，节点之间连接成树的结构，这个树结构与之前的树都不一样：
*   1. 是由子节点指向父节点
*   2. 在进行 union 操作的时候（比如 union(4,6)），要将4所在的树的根节点指向6所在的树的根节点。
* - 仍然是使用数组模拟：                                                                                                          1  2
*   0  1  2  3  4  5               0  1  2     4  5               0  1  2     4                 0  1  2                          /|\
*   ----------------   union(3,2)        |            union(5,3)       / \         union(4,0)   |    / \         union(4,5)     3 5 0
*   0  1  2  3  4  5      --->           3               --->         3  5            --->      4   3  5            --->            |
*                                  ----------------               ----------------              ----------------                    4
*                                  0  1  2  2  4  5               0  1  2  2  4  2              0  1  2  2  0  2            ----------------
*                                                                                                                           2  1  2  2  0  2
* - 这样实现的并查集的 union(p,q) 和 isConnected(p,q) 操作的时间复杂度都是 O(h)，其中 h 为 p, q 所在的树的最大高度。
*   这也是因为 find 操作的 复杂度是 O(h)。比起 UnionFind1，UnionFind2 的 union 操作复杂度减小，而 isConnected 复杂度增大。
* */

public class UnionFind2 implements UF {
    private int[] setIds;

    public UnionFind2(int size) {
        setIds = new int[size];
        for (int i = 0; i < size; i++)
            setIds[i] = i;
    }

    @Override
    public boolean isConnected(int p, int q) {  // 查看两个元素是否属于同一个集合，O(h) 复杂度
        return find(p) == find(q);
    }

    @Override
    public void union(int p, int q) {  // 合并两个元素所属的集合，O(h) 复杂度
        int pRoot = find(p);  // 比如 p 上上图4中的4，则 pRoot 是0
        int qRoot = find(q);  // 比如 q 上上图4中的0，则 qRoot 是2

        if (pRoot == qRoot)
            return;

        setIds[pRoot] = qRoot;  // 将 p 所在的根节点的集合编号置为 q 所在的根节点的集合编号
    }

    private int find(int p) {  // 查找元素 p 所对应的集合编号，O(h) 复杂度
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        while (setIds[p] != p)  // 循环找到 p 的根节点，只有根节点的值才等于其 setId，因此找到了根节点就找到了 p 所对应的集合编号
            p = setIds[p];
        return p;
    }

    @Override
    public int getSize() { return setIds.length; }
}
