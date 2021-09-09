package UnionFind;

/*
 * Size-based Optimization
 *
 * - 在 UnionFind2 的基础上加入了基于树上节点个数 size 的优化，将 size 小的树合并到 size 大的树上，使得合并后的树
 *   有很大几率高度更小。
 * - 演示 SEE: https://coding.imooc.com/lesson/207.html#mid=14168（11'34''）
 * */

public class UnionFind3 implements UF {
    private int[] parents;
    private int[] sizes;  // sizes[i] 表示以 i 为根的集合中的元素个数（即以 i 为根的树上的元素个数）

    public UnionFind3(int size) {
        parents = new int[size];
        sizes = new int[size];
        for (int i = 0; i < size; i++) {
            parents[i] = i;
            sizes[i] = 1;  // 每棵树的初始 size 都为1
        }
    }

    @Override
    public void union(int p, int q) {  // 合并两个元素所属的集合，O(h) 复杂度
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot) return;

        if (sizes[pRoot] < sizes[qRoot]) {  // 判断 p, q 所在的树的 size，将小者的根节点连接到大者的根节点上（加上该优化后性能大幅提升）
            parents[pRoot] = qRoot;
            sizes[qRoot] += sizes[pRoot];
        } else {
            parents[qRoot] = pRoot;
            sizes[pRoot] += sizes[qRoot];
        }
    }

    private int find(int p) {  // 查找元素 p 的集合编号，O(h) 复杂度
        if (p < 0 || p >= parents.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        while(parents[p] != p)
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

