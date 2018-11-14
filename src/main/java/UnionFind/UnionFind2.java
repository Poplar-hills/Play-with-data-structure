package UnionFind;

/*
* - 在 UnionFind2 这个实现主要解决的是 union 操作的时间复杂度是 O(n) 的问题。
* - 实现思路是，将数组中的每个元素看做是一个节点，节点之间连接成树的结构，这个树结构与之前的树都不一样：
*   1. 是由子节点指向父节点
*   2. 在进行 union 操作的时候（比如 union(4, 6)），要将4所在的根节点指向6所在的根节点。
* - 具体实现仍然是使用数组模拟：
*   1  2  3  4  5  6                1  2  3  5  6               1  2  3  5                 1  2  3                 2  3
*   ----------------   union(4, 3)        |        union(6, 4)       / \     union(5, 1)   |    / \   union(5, 6)    /|\
*   1  2  3  4  5  6      --->            4           --->          4  6        --->       5   4  6      --->       4 6 1
*                                   -------------               ----------                 --------                     |
*                                   1  2  3  5  6               1  2  3  5                 1  2  3                      5
*                                                                                                                  ------
*                                                                                                                  2  3
* - 这样设计的并查集的 union 和 isConnected 操作的时间复杂度都是 O(h)，h 为树的高度，因为 find 操作的 复杂度是 O(h)。
* */

public class UnionFind2 implements UF {
    private int[] setIds;

    public UnionFind2(int size) {
        setIds = new int[size];
        for (int i = 0; i < size; i++)  // 初始化时给每一个元素一个不同的集合编号，即有多少个元素就有多少个集合
            setIds[i] = i;
    }

    private int find(int p) {  // 查找元素 p 所对应的集合编号，O(h) 复杂度
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        while(setIds[p] != p)  // 循环找到 p 的根节点，只有根节点的值才等于其 setId，因此找到了根节点就找到了 p 所对应的集合编号
            p = setIds[p];
        return p;
    }

    @Override
    public boolean isConnected(int p, int q) {  // 查看两个元素是否属于同一个集合，O(h) 复杂度
        return find(p) == find(q);
    }

    @Override
    public void union(int p, int q) {  // 合并两个元素所属的集合，O(h) 复杂度
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot)
            return;

        setIds[pRoot] = qRoot;  // qRoot == setId[qRoot]
    }

    @Override
    public int getSize() { return setIds.length; }
}
